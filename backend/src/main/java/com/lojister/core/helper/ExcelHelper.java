package com.lojister.core.helper;

import com.lojister.business.abstracts.dynamic.*;
import com.lojister.controller.advertisement.SaveAdvertisementExcelRequest;
import com.lojister.core.exception.EmptyStringException;
import com.lojister.core.exception.ExcelException;
import com.lojister.mapper.ClientAdvertisementExcelSaveMapper;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.adresses.District;
import com.lojister.model.entity.adresses.Neighborhood;
import com.lojister.model.entity.adresses.Province;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.repository.address.DistrictRepository;
import com.lojister.repository.address.NeighborhoodRepository;
import com.lojister.repository.address.ProvinceRepository;
import com.lojister.business.concretes.AdvertisementLogic;
import com.lojister.core.util.LocalDateTimeParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class ExcelHelper {


    private final LocalDateTimeParseUtil localDateTimeParseUtil;
    private final AdvertisementLogic advertisementLogic;
    private final CargoTypeService cargoTypeService;
    private final LoadTypeService loadTypeService;
    private final PackagingTypeService packagingTypeService;
    private final TrailerFeatureService trailerFeatureService;
    private final TrailerFloorTypeService trailerFloorTypeService;
    private final TrailerTypeService trailerTypeService;
    private final VehicleTypeService vehicleTypeService;
    private final ClientAdvertisementExcelSaveMapper clientAdvertisementExcelSaveMapper;
    private final CurrencyUnitService currencyUnitService;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final NeighborhoodRepository neighborhoodRepository;

    @Autowired
    public ExcelHelper(LocalDateTimeParseUtil localDateTimeParseUtil, AdvertisementLogic advertisementLogic,
                       CargoTypeService cargoTypeService, LoadTypeService loadTypeService,
                       PackagingTypeService packagingTypeService, TrailerFeatureService trailerFeatureService,
                       TrailerFloorTypeService trailerFloorTypeService, TrailerTypeService trailerTypeService,
                       VehicleTypeService vehicleTypeService, ClientAdvertisementExcelSaveMapper clientAdvertisementExcelSaveMapper,
                       CurrencyUnitService currencyUnitService, ProvinceRepository provinceRepository,
                       DistrictRepository districtRepository, NeighborhoodRepository neighborhoodRepository) {
        this.localDateTimeParseUtil = localDateTimeParseUtil;
        this.advertisementLogic = advertisementLogic;
        this.cargoTypeService = cargoTypeService;
        this.loadTypeService = loadTypeService;
        this.packagingTypeService = packagingTypeService;
        this.trailerFeatureService = trailerFeatureService;
        this.trailerFloorTypeService = trailerFloorTypeService;
        this.trailerTypeService = trailerTypeService;
        this.vehicleTypeService = vehicleTypeService;
        this.clientAdvertisementExcelSaveMapper = clientAdvertisementExcelSaveMapper;
        this.currencyUnitService = currencyUnitService;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.neighborhoodRepository = neighborhoodRepository;
    }

    public String findProvinceByProvinceName(String provinceName) {

        Optional<Province> province = provinceRepository.findByProvinceNameIgnoreCase(provinceName);

        if (!province.isPresent()) {
            return "";
        }

        return province.get().getProvinceName();

    }

    public String findDistrictByProvinceNameAndDistrictName(String provinceName, String districtName) {

        Optional<District> district = districtRepository.findByProvinceNameIgnoreCaseAndDistrictNameIgnoreCase(provinceName, districtName);

        if (!district.isPresent()) {
            return "";
        }

        return district.get().getDistrictName();
    }


    public String findNeighborhoodByProvinceNameAndDistrictNameAndNeighborhoodName(String provinceName, String districtName, String neighborhoodName) {

        Optional<Neighborhood> neighborhood = neighborhoodRepository.findFirstByProvinceNameIgnoreCaseAndDistrictNameIgnoreCaseAndNeighborhoodNameContainsIgnoreCase(provinceName, districtName, neighborhoodName);

        if (!neighborhood.isPresent()) {
            return "";
        }

        return neighborhood.get().getNeighborhoodName();

    }

    public boolean selectBooleanValue(String value) {

        if (StringUtils.isBlank(value)) {
            throw new EmptyStringException("Yanlış Değer Yolladınız.");
        }
        if (value.equals("EVET")) {
            return true;
        } else if (value.equals("HAYIR")) {
            return false;
        } else {
            throw new EmptyStringException("Yanlış Değer Yolladınız.");
        }
    }


    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Yükleme Tarihi", "Yükleme Saati", "Varış Tarihi", "Varış Saati", "İlan Tipi", "Araç Sayısı", "Mal Değeri", "Para Birimi", "Parça Sayısı"
            , "Araç Tipi", "Dorse Tipi", "Dorse Zemin Tipi", "Dorse Özellikleri", "Yük Tipleri", "Yükleme Tipi", "Paketleme Tipi", "Teslim Edecek İsim", "Teslim Edecek Soyisim"
            , "Teslim Edecek Numara", "Teslim Edecek Şirket", "Teslim Alacak İsim", "Teslim Alacak Soyisim", "Teslim Alacak Numara", "Teslim Alacak Şirket", "Hacim", "Desi", "Ldm", "Ağırlık"
            , "Yükseklik", "Uzunluk", "Genişlik", "Hammaliye Olsun Mu", "İstifleme Yapılacak Mı", "Açıklama", "Doküman Numarası", "Başlangıç Ülke", "Başlangıç İl", "Başlangıç İlçe"
            , "Başlangıç Mahalle", "Başlangıç Sokak", "Başlangıç Bina Bilgisi", "Başlangıç Adres Tanımı", "Bitiş Ülke", "Bitiş İl", "Bitiş İlçe"
            , "Bitiş Mahalle", "Bitiş Sokak", "Bitiş Bina Bilgisi", "Bitiş Adres Tanımı"};

    static String SHEET = "Advertisement";


    public boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }


    public String nullCheckString(String cellValue) {

        if (StringUtils.isBlank(cellValue)) {
            cellValue = "";
        }
        return cellValue.trim();
    }

    public List<String> splitStringOfExcel(String excelString) {

        if (StringUtils.isBlank(excelString)) {
            throw new EmptyStringException("Excel İçindeki Değer Boş Geçilemez.");
        }

        if (excelString.contains(",")) {

            String[] arrOfSplit = excelString.split(",");

            List<String> splitStringList = new ArrayList<>();

            for (String value : arrOfSplit) {

                if (StringUtils.isNotBlank(value)) {

                    splitStringList.add(value.trim());
                }

            }

            return splitStringList;
        } else {

            return Collections.singletonList(excelString.trim());
        }

    }

    public String splitStringSpace(String string) {

        if (StringUtils.isBlank(string)) {
            throw new EmptyStringException("Boş Değer Yollanamaz");
        }

        String[] parts = string.split(" ");
        return parts[0];

    }

    /*
    public static ByteArrayInputStream clientAdvertisementToExcel(List<ExcelClientAdvertisementSaveDto> excelClientAdvertisementSaveDtoList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            int rowIdx = 1;
            for (Tutorial tutorial : tutorials) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(tutorial.getId());
                row.createCell(1).setCellValue(tutorial.getTitle());
                row.createCell(2).setCellValue(tutorial.getDescription());
                row.createCell(3).setCellValue(tutorial.isPublished());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
     */

    public List<SaveAdvertisementExcelRequest> excelToClientAdvertisement(InputStream is) {
        try {

            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            DataFormatter formatter = new DataFormatter();

            List<ClientAdvertisement> clientAdvertisementList = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                ClientAdvertisement clientAdvertisement = new ClientAdvertisement();
                Recipient startRecipient = new Recipient();
                Recipient dueRecipient = new Recipient();

                AdvertisementAddress startingAddress = new AdvertisementAddress();
                AdvertisementAddress dueAddress = new AdvertisementAddress();


                int cellIdx = 0;
                while (cellsInRow.hasNext()) {

                    Cell currentCell = cellsInRow.next();

                  /*  switch (currentCell.getColumnIndex()) {
                        case 0:
                            clientAdvertisement.setAdStartingDate(localDateTimeParseUtil.getLocalDate(formatter.formatCellValue(currentCell)));
                            break;

                        case 1:
                            clientAdvertisement.setAdStartingTime(localDateTimeParseUtil.getLocalTime(formatter.formatCellValue(currentCell)));
                            //  clientAdvertisement.setAdStartingTime(localDateTimeParseUtil.getLocalTime(currentCell.getStringCellValue()));
                            break;

                        case 2:
                            clientAdvertisement.setAdDueDate(localDateTimeParseUtil.getLocalDate(formatter.formatCellValue(currentCell)));
                            break;

                        case 3:
                            clientAdvertisement.setAdDueTime(localDateTimeParseUtil.getLocalTime(formatter.formatCellValue(currentCell)));
                            break;

                        case 4:
                            clientAdvertisement.setClientAdvertisementType(advertisementLogic.stringToClientAdvertisementType(currentCell.getStringCellValue()));
                            break;

                        case 5:

                            if (!(currentCell == null || currentCell.getCellType() == CellType.BLANK)) {
                                clientAdvertisement.setVehicleCount((long) currentCell.getNumericCellValue());
                            }
                            break;

                        case 6:
                            clientAdvertisement.setGoodsPrice(Double.valueOf(formatter.formatCellValue(currentCell)));
                            break;

                        case 7:
                            clientAdvertisement.setCurrencyUnit(currencyUnitService.findCurrencyUnitByCurrencyAbbreviation(nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 8:
                            clientAdvertisement.setPiece((long) currentCell.getNumericCellValue());
                            break;

                        case 9:
                            clientAdvertisement.setVehicleTypes(vehicleTypeService.findSetVehicleTypesByTypeNameList(splitStringOfExcel(currentCell.getStringCellValue())));
                            break;

                        case 10:
                            clientAdvertisement.setTrailerTypes(trailerTypeService.findSetTrailerTypesByTypeNameList(splitStringOfExcel(currentCell.getStringCellValue())));
                            break;

                        case 11:
                            clientAdvertisement.setTrailerFloorTypes(trailerFloorTypeService.findSetTrailerFloorTypesByTypeNameList(splitStringOfExcel(currentCell.getStringCellValue())));
                            break;

                        case 12:
                            clientAdvertisement.setTrailerFeatures(trailerFeatureService.findSetTrailerFeaturesByFeatureNameList(splitStringOfExcel(currentCell.getStringCellValue())));
                            break;

                        case 13:
                            clientAdvertisement.setCargoTypes(cargoTypeService.findSetCargoTypesByNameList(splitStringOfExcel(currentCell.getStringCellValue())));
                            break;

                        case 14:
                            clientAdvertisement.setLoadType(loadTypeService.findSetLoadTypeByTypeNameList(splitStringOfExcel(currentCell.getStringCellValue())));
                            break;

                        case 15:
                            clientAdvertisement.setPackagingType(packagingTypeService.findDataByTypeName(currentCell.getStringCellValue()));
                            break;

                        case 16:
                            startRecipient.setFirstName(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 17:
                            startRecipient.setLastName(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 18:
                            startRecipient.setPhoneNumber(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 19:
                            startRecipient.setCommercialTitle(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 20:
                            dueRecipient.setFirstName(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 21:
                            dueRecipient.setLastName(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 22:
                            dueRecipient.setPhoneNumber(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 23:
                            dueRecipient.setCommercialTitle(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 24:
                            clientAdvertisement.setVolume(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 25:
                            clientAdvertisement.setDesi(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 26:
                            clientAdvertisement.setLdm(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 27:
                            clientAdvertisement.setTonnage(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 28:
                            clientAdvertisement.setHeight(currentCell.getNumericCellValue());
                            break;

                        case 29:
                            clientAdvertisement.setLength(currentCell.getNumericCellValue());
                            break;

                        case 30:
                            clientAdvertisement.setWidth(currentCell.getNumericCellValue());
                            break;

                        case 31:
                            clientAdvertisement.setIsPorter(selectBooleanValue(currentCell.getStringCellValue()));
                            break;

                        case 32:
                            clientAdvertisement.setIsStacking(selectBooleanValue(currentCell.getStringCellValue()));
                            break;

                        case 33:
                            clientAdvertisement.setExplanation(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 34:
                            clientAdvertisement.setDocumentNo(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 35:
                            startingAddress.setCountry(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 36:
                            startingAddress.setProvince(findProvinceByProvinceName(nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 37:
                            startingAddress.setDistrict(findDistrictByProvinceNameAndDistrictName(startingAddress.getProvince(), nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 38:
                            startingAddress.setNeighborhood(findNeighborhoodByProvinceNameAndDistrictNameAndNeighborhoodName(startingAddress.getProvince(), startingAddress.getDistrict(), nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 39:
                            startingAddress.setStreet(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 40:
                            startingAddress.setBuildingInformation(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 41:
                            startingAddress.setFullAddress(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 42:
                            dueAddress.setCountry(nullCheckString(currentCell.getStringCellValue()));
                            break;

                        case 43:
                            dueAddress.setProvince(findProvinceByProvinceName(nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 44:
                            dueAddress.setDistrict(findDistrictByProvinceNameAndDistrictName(dueAddress.getProvince(), nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 45:
                            dueAddress.setNeighborhood(findNeighborhoodByProvinceNameAndDistrictNameAndNeighborhoodName(dueAddress.getProvince(), dueAddress.getDistrict(), nullCheckString(currentCell.getStringCellValue())));
                            break;

                        case 46:
                            dueAddress.setStreet(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 47:
                            dueAddress.setBuildingInformation(nullCheckString(formatter.formatCellValue(currentCell)));
                            break;

                        case 48:
                            dueAddress.setFullAddress(nullCheckString(currentCell.getStringCellValue()));
                            break;


                        default:
                            break;
                    }*/

                    cellIdx++;

                }
                clientAdvertisement.setStartRecipient(startRecipient);
                clientAdvertisement.setDueRecipient(dueRecipient);

                clientAdvertisement.setStartingAddress(startingAddress);
                clientAdvertisement.setDueAddress(dueAddress);


                clientAdvertisementList.add(clientAdvertisement);

            }

            workbook.close();

            return clientAdvertisementExcelSaveMapper.entityListToDtoList(clientAdvertisementList);

        } catch (IOException e) {
            throw new ExcelException("Excel Dosyasında Hata Meydana Gelmiştir : " + e.getMessage());
        }
    }

}