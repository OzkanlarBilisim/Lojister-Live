package com.lojister.core.config.db;

import com.lojister.model.dto.GeoCodeAddressDto;
import com.lojister.model.dto.PositionDto;
import com.lojister.model.entity.*;
import com.lojister.model.entity.client.*;
import com.lojister.model.entity.driver.DriverNotificationSetting;
import com.lojister.model.enums.*;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.account.UserRepository;
import com.lojister.repository.address.SavedAddressRepository;
import com.lojister.repository.advertisement.*;
import com.lojister.repository.bank.BankInformationRepository;
import com.lojister.repository.client.ClientRepository;
import com.lojister.repository.company.CompanyRepository;
import com.lojister.repository.currencyunit.CurrencyUnitRepository;
import com.lojister.repository.driver.DriverDocumentTypeRepository;
import com.lojister.repository.driver.DriverRepository;
import com.lojister.repository.site.AboutUsRepository;
import com.lojister.repository.site.FrequentlyAskedQuestionsRepository;
import com.lojister.repository.trailer.TrailerFeatureRepository;
import com.lojister.repository.trailer.TrailerFloorTypeRepository;
import com.lojister.repository.trailer.TrailerTypeRepository;
import com.lojister.repository.transport.ClientTransportProcessRepository;
import com.lojister.repository.vehicle.VehicleDocumentTypeRepository;
import com.lojister.repository.vehicle.VehicleTypeRepository;
import com.lojister.service.gmaps.GMapsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final BCryptPasswordEncoder encoder;
    private final DriverDocumentTypeRepository driverDocumentTypeRepository;
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final BankInformationRepository bankInformationRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final AboutUsRepository aboutUsRepository;
    private final VehicleDocumentTypeRepository vehicleDocumentTypeRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final TrailerTypeRepository trailerTypeRepository;
    private final TrailerFeatureRepository trailerFeatureRepository;
    private final TrailerFloorTypeRepository trailerFloorTypeRepository;
    private final LoadTypeRepository loadTypeRepository;
    private final CargoTypeRepository cargoTypeRepository;
    private final PackagingTypeRepository packagingTypeRepository;
    private final FrequentlyAskedQuestionsRepository frequentlyAskedQuestionsRepository;
    private final CurrencyUnitRepository currencyUnitRepository;
    private final ClientAdvertisementRepository clientAdvertisementRepository;
    private final GMapsService gMapsService;
    private final ClientAdvertisementBidRepository clientAdvertisementBidRepository;
    private final ClientAdvertisementContainerRepository clientAdvertisementContainerRepository;
    private final SavedAddressRepository savedAddressRepository;
    private final ClientTransportProcessRepository clientTransportProcessRepository;

    private void createDynamicAdminPanelData() {


        DriverDocumentType driverDocumentType1 = new DriverDocumentType("Nüfus Cüzdanı (Ön Yüz)", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType1);
        DriverDocumentType driverDocumentType2 = new DriverDocumentType("Nüfus Cüzdanı (Arka Yüz)", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType2);
        DriverDocumentType driverDocumentType3 = new DriverDocumentType("Ehliyet (Ön Yüz)", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType3);
        DriverDocumentType driverDocumentType4 = new DriverDocumentType("Ehliyet (Arka Yüz)", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType4);
        DriverDocumentType driverDocumentType5 = new DriverDocumentType("SRC Belgesi", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType5);
        DriverDocumentType driverDocumentType6 = new DriverDocumentType("Psikoteknik Belgesi", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType6);
        DriverDocumentType driverDocumentType7 = new DriverDocumentType("İş Güvenliği Belgesi", DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType7);

        VehicleDocumentType vehicleDocumentType1 = new VehicleDocumentType("Ruhsat (Çekici)", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType1);
        VehicleDocumentType vehicleDocumentType2 = new VehicleDocumentType("Ruhsat (Dorse)", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType2);
        VehicleDocumentType vehicleDocumentType3 = new VehicleDocumentType("Araç Muayene Belgesi (Çekici)", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType3);
        VehicleDocumentType vehicleDocumentType4 = new VehicleDocumentType("Araç Muayene Belgesi (Dorse)", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType4);
        VehicleDocumentType vehicleDocumentType5 = new VehicleDocumentType("Taşımacılık Belgesi", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType5);
        VehicleDocumentType vehicleDocumentType6 = new VehicleDocumentType("Trafik Sigorta Poliçesi", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType6);
        VehicleDocumentType vehicleDocumentType7 = new VehicleDocumentType("Yük Sigorta Poliçesi", DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType7);

        VehicleType vehicleType1 = new VehicleType("Tır", DynamicStatus.ACTIVE);
        vehicleTypeRepository.save(vehicleType1);
        VehicleType vehicleType2 = new VehicleType("Kırkayak", DynamicStatus.ACTIVE);
        vehicleTypeRepository.save(vehicleType2);
        VehicleType vehicleType3 = new VehicleType("Kamyon", DynamicStatus.ACTIVE);
        vehicleTypeRepository.save(vehicleType3);
        VehicleType vehicleType4 = new VehicleType("Kamyonet", DynamicStatus.ACTIVE);
        vehicleTypeRepository.save(vehicleType4);

        TrailerType trailerType1 = new TrailerType("Tenteli", DynamicStatus.ACTIVE);
        trailerTypeRepository.save(trailerType1);
        TrailerType trailerType2 = new TrailerType("Açık", DynamicStatus.ACTIVE);
        trailerTypeRepository.save(trailerType2);
        TrailerType trailerType3 = new TrailerType("Kapalı", DynamicStatus.ACTIVE);
        trailerTypeRepository.save(trailerType3);
        TrailerType trailerType4 = new TrailerType("Sal Kasa", DynamicStatus.ACTIVE);
        trailerTypeRepository.save(trailerType4);
        TrailerType trailerType5 = new TrailerType("Frigorifik", DynamicStatus.ACTIVE);
        trailerTypeRepository.save(trailerType5);

        PackagingType packagingType1 = new PackagingType("Paletli", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType1);
        PackagingType packagingType2 = new PackagingType("Ambalajlı Kutu / Koli", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType2);
        PackagingType packagingType3 = new PackagingType("Varil / Bidon", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType3);
        PackagingType packagingType4 = new PackagingType("Big-Bag Çuval", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType4);
        PackagingType packagingType5 = new PackagingType("IBC Tank", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType5);
        PackagingType packagingType6 = new PackagingType("Bobin", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType6);
        PackagingType packagingType7 = new PackagingType("Dökme", DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType7);

        LoadType loadType1 = new LoadType("El ile", DynamicStatus.ACTIVE);
        loadTypeRepository.save(loadType1);
        LoadType loadType2 = new LoadType("Forklift ile Rampadan", DynamicStatus.ACTIVE);
        loadTypeRepository.save(loadType2);
        LoadType loadType3 = new LoadType("Forklift ile Yandan", DynamicStatus.ACTIVE);
        loadTypeRepository.save(loadType3);
        LoadType loadType4 = new LoadType("Transpalet", DynamicStatus.ACTIVE);
        loadTypeRepository.save(loadType4);
        LoadType loadType5 = new LoadType("Vinç", DynamicStatus.ACTIVE);
        loadTypeRepository.save(loadType5);

        CargoType cargoType1 = new CargoType("Elektronik Malzeme", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType1);
        CargoType cargoType2 = new CargoType("Gıda (Kuru)", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType2);
        CargoType cargoType3 = new CargoType("Gıda (Sıvı)", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType3);
        CargoType cargoType4 = new CargoType("Ham Madde", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType4);
        CargoType cargoType5 = new CargoType("Kırılabilir Ürün", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType5);
        CargoType cargoType6 = new CargoType("Kimyasal Malzeme", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType6);
        CargoType cargoType7 = new CargoType("Medikal Malzeme", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType7);
        CargoType cargoType8 = new CargoType("Mermer", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType8);
        CargoType cargoType9 = new CargoType("Otomotiv Ürünü", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType9);
        CargoType cargoType10 = new CargoType("Plastik Malzeme", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType10);
        CargoType cargoType11 = new CargoType("Tarım Ürünü", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType11);
        CargoType cargoType12 = new CargoType("Tekstil Ürünü", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType12);
        CargoType cargoType13 = new CargoType("Yapı / İnşaat Malzemesi", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType13);
        CargoType cargoType14 = new CargoType("Diğer (Açıklamada Belirtiniz)", DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType14);

        TrailerFloorType trailerFloorType1 = new TrailerFloorType("Ahşap Taban", DynamicStatus.ACTIVE);
        trailerFloorTypeRepository.save(trailerFloorType1);
        TrailerFloorType trailerFloorType2 = new TrailerFloorType("Saç Taban", DynamicStatus.ACTIVE);
        trailerFloorTypeRepository.save(trailerFloorType2);

        TrailerFeature trailerFeature1 = new TrailerFeature("Tavan Açılır", DynamicStatus.ACTIVE);
        trailerFeatureRepository.save(trailerFeature1);
        TrailerFeature trailerFeature2 = new TrailerFeature("Yanlar Açılır", DynamicStatus.ACTIVE);
        trailerFeatureRepository.save(trailerFeature2);
        TrailerFeature trailerFeature3 = new TrailerFeature("Jumbo", DynamicStatus.ACTIVE);
        trailerFeatureRepository.save(trailerFeature3);
        TrailerFeature trailerFeature4 = new TrailerFeature("Midilli", DynamicStatus.ACTIVE);
        trailerFeatureRepository.save(trailerFeature4);
        TrailerFeature trailerFeature5 = new TrailerFeature("Babalar Çıkar", DynamicStatus.ACTIVE);
        trailerFeatureRepository.save(trailerFeature5);

        CurrencyUnit currencyUnit1 = new CurrencyUnit("Türk Lirası", "TL", "₺", DynamicStatus.ACTIVE);
        currencyUnitRepository.save(currencyUnit1);
        CurrencyUnit currencyUnit2 = new CurrencyUnit("Dolar", "USD", "$", DynamicStatus.ACTIVE);
        currencyUnitRepository.save(currencyUnit2);
        CurrencyUnit currencyUnit3 = new CurrencyUnit("Euro", "EUR", "€", DynamicStatus.ACTIVE);
        currencyUnitRepository.save(currencyUnit3);

        Optional<AboutUs> aboutUs = aboutUsRepository.findFirstByOrderByIdAsc();

        if (!(aboutUs.isPresent())) {

            AboutUs aboutUs1 = new AboutUs();
            aboutUs1.setEng_explanation("{\"blocks\":[{\"key\":\"ekr50\",\"text\":\".\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{}}");
            aboutUs1.setTr_explanation("{\"blocks\":[{\"key\":\"8god9\",\"text\":\"Teknolojinin hızla gelişmesi küreselleşmeyi hızlandırırken birçok sektörü de dijitalleşmeye yönlendirmiştir. Dijitalleşme ile birlikte geleneksel yöntemlerle yapılan işlemler ve yürütülen süreçler sanal ortama taşınarak, kullanıcılara daha hızlı, güvenli ve ekonomik çözümler sunulabilmektedir.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"e8aa8\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"76dvq\",\"text\":\"Lojister, bu değişimi lojistik sektörüne taşıyarak, sektörel verimliliği en üst seviyeye çıkarır, kullanılan yapay zeka teknolojileri ile kullanıcılarının zaman ve maliyet tasarrufu sağlayarak verimliliklerini arttıran ve operasyonel yönetimlerini kolaylaştıran bir altyapı sunar.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"4nm98\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"9hhkq\",\"text\":\"Lojister yük veren ve nakliye firmalarını aynı çatı altında birleştiren dijital bir platformdur. Platforma dahil olan nakliye firmaları detaylı bir şekilde incelenerek güvenli bir ortam hazırlanmıştır.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"evtub\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"6rb0h\",\"text\":\"Lojister gelişmiş altyapısı ve teknolojileri ile bu sektörde yaşanan problemleri çözerek, UI ve UX tasarımlarıyla kullanıcılarının sistemden daha verimli bir şekilde yararlanmasını sağlamaktadır.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"ch32v\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"6knab\",\"text\":\"Kullanılan konum tabanlı yük takip sistemi kullanıcılarına araçları 7/24 canlı takip olanağı sunar. Kullanıcıların geçmişe dönük maliyet hesaplamaları yapabilmesi, geçmiş işlemlerini inceleyebilmesi için Lojister raporlama sistemi geliştirilmiştir. Bu sayede platform üzerinde yapılan her anlaşma ve operasyon kayıt altına alınarak kullanıcılarının geçmiş verilerine kolaylıkla ulaşması sağlanmaktadır.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"aelf7\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"2rohq\",\"text\":\"Yapay zeka algoritmalarıyla taşıyıcı firmaların daha kolay yük bulmasını sağlayan Lojister, kullanıcılarına daha yola çıkmadan dönüş yükü bulabilme fırsatını sunarken, sahip olduğu teknolojiler ile taşıyıcı firmaların ve yük sahiplerinin lojistik operasyonlarını etkili yönetmelerini sağlayabilmeleri adına kullanıcılarına inovatif çözümler sunmaktadır.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"39vb6\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"blcrc\",\"text\":\"Lojister , hem yük sahibi hem de taşıyıcı firmaların güvenle işlemlerini yapabildiği, yaşanan aksaklıklarda tarafların hakları korunarak lojistik operasyonların titizlik ve şeffaflık içinde yürütülmesi için ideal altyapıyı sunar.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"fcu5f\",\"text\":\"\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}},{\"key\":\"a9h6g\",\"text\":\"Yapılan pazar araştırmaları ve bu araştırmaların sonucunda ortaya konmuş problemler üzerinde Ar-Ge çalışmaları yürüterek oluşturulmuş bir proje olan Lojister, “Lojistikte Ana Yol” sloganıyla lojistik sektöründe dijital iş ortağınız olma prensibiyle bu sektörün öncüsü olma yolunda ilerlemektedir.\",\"type\":\"unstyled\",\"depth\":0,\"inlineStyleRanges\":[],\"entityRanges\":[],\"data\":{}}],\"entityMap\":{}}");
            aboutUsRepository.save(aboutUs1);
        }

        FrequentlyAskedQuestions frequentlyAskedQuestions1 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions1.setTr_question("Lojister nedir ?");
        frequentlyAskedQuestions1.setTr_answer("Lojister, yük sahiplerinin ihtiyaçlarını ilanlarda belirterek hızlı ve güvenli bir şekilde nakliye ihtiyaçlarını karşıladıkları, yük taşıyanların ise ilanlara teklif vererek yük bulduğu dijital bir platformdur.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions1);

        FrequentlyAskedQuestions frequentlyAskedQuestions2 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions2.setTr_question("Lojister bir lojistik şirketi midir ?");
        frequentlyAskedQuestions2.setTr_answer("Lojister bir lojistik şirketi değildir. Lojister yük veren ve nakliye firmalarını aynı çatı altında birleştirerek daha hızlı ve güvenli bir şekilde ihtiyaçlarına çözüm bulmalarını sağlayan bir platformdur. Lojistik operasyonların canlı olarak takibinin yapılmasının yanı sıra geçmişte yaptığınız işlemleri inceleyebildiğiniz maaliyet analizlerini yapabildiğiniz bir sistem olan Lojister, lojistik maaliyetleri en aza indirgeyerek sektörel verimliliği arttırmaktadır.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions2);

        FrequentlyAskedQuestions frequentlyAskedQuestions3 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions3.setTr_question("İhtiyacıma uygun nakliye aracı veya yük nasıl bulabilirim ?");
        frequentlyAskedQuestions3.setTr_answer("Yük sahipleri ihtiyaçları doğrultusunda ilan açarlar. Nakliye firmaları platformda bulunan ilanlara yeterlilikleri doğrultusunda teklif verirler. Yük sahipleri gelen teklifler arasında ihtiyaçlarına en uygun teklifi kabul ederek ödemelerini yaparlar. Yapılan ödeme lojister garantörlüğünde saklı tutularak iş akdinin tamamlanmasıyla nakliye firmasına aktarılır.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions3);

        FrequentlyAskedQuestions frequentlyAskedQuestions4 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions4.setTr_question("Lojister ile çalışmanın avantajları nelerdir ?");
        frequentlyAskedQuestions4.setTr_answer("Lojister, kolay kullanımı ve bünyesinde bulundurduğu çeşitli teknolojik yeniliklerle kullanıcılarının operasyonel yönetim süreçlerini daha titiz ve şeffaf şekilde yönetmelerine olanak sağlar.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions4);

        FrequentlyAskedQuestions frequentlyAskedQuestions5 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions5.setTr_question("Lojister'a üyelik ücretsiz midir ?");
        frequentlyAskedQuestions5.setTr_answer("Lojister'a üyelik tamamen ücretsizdir. Hemen üye olarak teklif bekleyen ilanları görebilir, teklif verebilir veya ihtiyaçlarınıza uygun yeni ilanlar oluşturabilirsiniz.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions5);

        FrequentlyAskedQuestions frequentlyAskedQuestions6 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions6.setTr_question("Ödeme yöntemleri nelerdir ?");
        frequentlyAskedQuestions6.setTr_answer("Yapılan anlaşmalar sonucunda ödenecek olan meblağ sanal pos yöntemiyle 3d secure güvencesinde kredi kartı veya banka kartıyla gerçekleştirilebilmektedir.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions6);

        FrequentlyAskedQuestions frequentlyAskedQuestions7 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions7.setTr_question("Nakliyecilere nasıl güvenebilirim ?");
        frequentlyAskedQuestions7.setTr_answer("Platformumuza üye olan nakliye firmaları titizlikle incelenmiş olup, kurumsal firmalardan oluşmaktadır ve bu firmaların araç ve şoför bilgileri kayıt altına alınıp sürekli güncel tutulmaktadır. Firmaların daha önceden platformumuz üzerinde gerçekleştirdikleri operasyonlar sonucu aldıkları puanları inceleyebilir, yorumları okuyabilirsiniz.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions7);

        FrequentlyAskedQuestions frequentlyAskedQuestions8 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions8.setTr_question("Yükümün güvende olduğundan nasıl emin olabilirim ?");
        frequentlyAskedQuestions8.setTr_answer("Lojister yükünüzün ve tüm nakliye sürecinizin takibini canlı olarak izleme olanağı sunmaktadır. Herhangi bir sorun ile karşılaşıldığında size konuyla ilgili bildirim gelmektedir. ");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions8);

        FrequentlyAskedQuestions frequentlyAskedQuestions9 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions9.setTr_question("Nakliye bedeli nasıl belirleniyor ?");
        frequentlyAskedQuestions9.setTr_answer("Lojister, nakliye bedelinin yük veren ve nakliye firmalarının arasında belirlendiği bir platformdur.  ");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions9);

        FrequentlyAskedQuestions frequentlyAskedQuestions10 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions10.setTr_question("Geçmişteki nakliye bilgilerime ulaşabilir miyim ?");
        frequentlyAskedQuestions10.setTr_answer("Lojister yaptığınız nakliye işlemlerini kayıt altında tutar ve istediğiniz zaman geçmişe dönük inceleme yapabilme olanağını sağlar.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions10);

        FrequentlyAskedQuestions frequentlyAskedQuestions11 = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions11.setTr_question("Yük takip sistemi nedir ?");
        frequentlyAskedQuestions11.setTr_answer("Yük takip sistemi gerçek zamanlı konum takip sistemidir. Yükünüz yola çıktıktan sonra 7-24 takip edebilme olanağına sahip olarak, hangi rota üzerinden geldiğini ve gittiğini inceleyebilmenize, park zamanlarını ve yerlerini görebilmenize olanak sağlayan bir sistemdir.");
        frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions11);

    }

    private void createInitialUserTwo() {
        DriverNotificationSetting driverNotificationSetting = new DriverNotificationSetting();
        driverNotificationSetting.setNewAdvertisementMailSending(Boolean.TRUE);
        driverNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.TRUE);

        ClientNotificationSetting clientNotificationSetting = new ClientNotificationSetting();
        clientNotificationSetting.setNewAdvertisementBidEmailSending(Boolean.TRUE);
        clientNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.TRUE);

        String passwordClient = encoder.encode("clientclient");

        Client client2 = new Client();

        client2.setCurrent(false);
        client2.setTl(0.0);
        client2.setUsd(0.0);
        client2.setEuro(0.0);

        client2.setFirstName("Client2 Firstname");
        client2.setLastName("Client2 Lastname");
        client2.setPhone("+902011231212");
        client2.setEmail("client2@client.com");
        client2.setPassword(passwordClient);
        client2.setClientType(ClientType.PERSON);
        client2.setRole(Role.ROLE_CLIENT);
        client2.setMailConfirmed(Boolean.TRUE);
        client2.setPhoneConfirmed(Boolean.TRUE);
        client2.setNotificationSetting(clientNotificationSetting);
        clientRepository.save(client2);

        //----------------------------------------------------------------

        String passwordDriver = encoder.encode("driverdriver");

        Driver driver2 = new Driver();
        driver2.setFirstName("Driver Firstname2");
        driver2.setLastName("Driver Lastname2");
        driver2.setPhone("+903011231212");
        driver2.setPassword(passwordDriver);
        driver2.setCitizenId("12345637812");
        driver2.setEmail("driver2@driver.com");
        driver2.setStatus(DriverStatus.ACCEPTED);
        driver2.setRole(Role.ROLE_DRIVER);
        driver2.setDriverTitle(DriverTitle.BOSS);
        driver2.setMailConfirmed(Boolean.TRUE);
        driver2.setPhoneConfirmed(Boolean.TRUE);
        driver2.setNotificationSetting(driverNotificationSetting);

        Company company2 = new Company();
        Address address2 = new Address();
        address2.setCountry("Country2");
        address2.setDistrict("District2");
        address2.setProvince("Province2");
        address2.setNeighborhood("Neighborhood2");
        address2.setFullAddress("Full Address2");

        BankInformation bankInformation2 = new BankInformation();
        bankInformation2.setAccountName("AccountName2");
        bankInformation2.setBankName("BankName2");
        bankInformation2.setIban("Iban2");
        bankInformation2.setBranch("Branch2");
        bankInformation2.setAccountNumber("AccountNumber2");
        bankInformation2 = bankInformationRepository.save(bankInformation2);

        company2.setCommercialTitle("commercial Title2");
        company2.setTaxNumber("tax number2");
        address2.setFullAddress("Denizli Aselsis Driver Full Adres2");
        company2.setAddress(address2);
        company2.setTaxAdministration("Tax Administration2");
        company2.setBankInformation(bankInformation2);
        company2.setFinancialStaffFirstname("FinancialStaffFirstname2");
        company2.setFinancialStaffLastname("FinancialStaffLastname2");
        company2.setFinancialStaffPhone("FinancialStaffPhone2");
        company2.setMail("company2@mail");
        company2.setPhone("CompanyPhone2");
        company2.setRating(10.0);
        company2 = companyRepository.save(company2);

        driver2.setCompany(company2);
        driverRepository.save(driver2);

    }

    private PositionDto createLanLng(GeoCodeAddressDto geoCodeAddressDto) {
        return gMapsService.geocodeFromAddress(geoCodeAddressDto);
    }

    private void updateAdvertisementProcessStatus() {
        List<ClientAdvertisement> clientAdvertisementList = clientAdvertisementRepository.findAll();
        clientAdvertisementList.stream().forEach((clientAdvertisement) -> {
            if (Optional.ofNullable(clientAdvertisement.getAdvertisementStatus()).isPresent()) {
                if (clientAdvertisement.getAdvertisementStatus() == AdvertisementStatus.ACTIVE) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.WAITING);
                }
            }
            if (!clientAdvertisementBidRepository.findByClientAdvertisement_Id(clientAdvertisement.getId()).isEmpty()) {
                clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.BID_GIVEN);
            }

            if (Optional.ofNullable(clientAdvertisement.getClientTransportProcess()).isPresent()) {
                if (Optional.ofNullable(clientAdvertisement.getClientTransportProcess().getAcceptedClientAdvertisementBid()).isPresent()) {

                    if (clientAdvertisement.getClientTransportProcess().getAcceptedClientAdvertisementBid().getBidStatus() == BidStatus.APPROVED) {
                        clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.BID_APPROVED);
                    }
                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.PAYMENT_SUCCESSFUL) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.PAYMENT_SUCCESSFUL);

                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.CARGO_ON_THE_WAY) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.WAITING_WAYBILL) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.WAITING_WAYBILL);
                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.WAYBILL_DENIED) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.WAYBILL_DENIED);
                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.CARGO_COULD_NOT_BE_DELIVERED) {

                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_COULD_NOT_BE_DELIVERED);
                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.CLIENT_CARGO_PROBLEM) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.CLIENT_CARGO_PROBLEM);
                }
                if (clientAdvertisement.getClientTransportProcess().getTransportProcessStatus() == TransportProcessStatus.COMPLETED) {
                    clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.COMPLETED);
                }
            }


        });
        clientAdvertisementRepository.saveAll(clientAdvertisementList);
    }

    private void updateClientAdvertisementCode() {

        List<ClientAdvertisement> clientAdvertisementList = clientAdvertisementRepository.findAll();
        AtomicReference<Long> value = new AtomicReference<>(0L);
        AtomicReference<String> swap = new AtomicReference<String>("");
        swap.set(clientAdvertisementList.get(0).getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        clientAdvertisementList.stream().forEach((clientAdvertisement) -> {
            String currentDate = clientAdvertisement.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            StringBuilder stringBuilder = new StringBuilder();
            if (swap.get().equals(currentDate)) {
                value.getAndSet(value.get() + 1);


            } else {
                value.set(0L);

            }
            String result = String.join("", Collections.nCopies(6 - String.valueOf(value).length(), "0")) + value;
            stringBuilder.append("CAD").append(currentDate).append(result);
            swap.set(currentDate);
            clientAdvertisement.setClientAdvertisementCode(stringBuilder.toString());
        });

        clientAdvertisementRepository.saveAll(clientAdvertisementList);
    }

    private void createSimpleAdvertisementAddress() {
      /* List<ClientAdvertisement> clientAdvertisementList =clientAdvertisementRepository.findAll();
        clientAdvertisementList.stream().forEach((clientAdvertisement)->{
            SimpleAdvertisementAddress simpleAdvertisementAddress= new SimpleAdvertisementAddress();
            simpleAdvertisementAddress.setCountry(clientAdvertisement.getStartingAddress().getCountry());
            simpleAdvertisementAddress.setDistrict(clientAdvertisement.getStartingAddress().getDistrict());
            simpleAdvertisementAddress.setNeighborhood(clientAdvertisement.getStartingAddress().getNeighborhood());
            simpleAdvertisementAddress.setProvince(clientAdvertisement.getStartingAddress().getProvince());
            GeoCodeAddressDto geoCodeAddressDto=new GeoCodeAddressDto();
            geoCodeAddressDto.setDistrict(simpleAdvertisementAddress.getDistrict());
            geoCodeAddressDto.setNeighborhood(simpleAdvertisementAddress.getNeighborhood());
            geoCodeAddressDto.setProvince(simpleAdvertisementAddress.getProvince());
            PositionDto positionDto= createLanLng(geoCodeAddressDto);
            simpleAdvertisementAddress.setLat(positionDto.getLatitude());
            simpleAdvertisementAddress.setLng(positionDto.getLongitude());
            clientAdvertisement.setSimpleStartingAddress(simpleAdvertisementAddress);

            SimpleAdvertisementAddress simpleAdvertisementAddress1= new SimpleAdvertisementAddress();
            simpleAdvertisementAddress1.setCountry(clientAdvertisement.getDueAddress().getCountry());
            simpleAdvertisementAddress1.setDistrict(clientAdvertisement.getDueAddress().getDistrict());
            simpleAdvertisementAddress1.setNeighborhood(clientAdvertisement.getDueAddress().getNeighborhood());
            simpleAdvertisementAddress1.setProvince(clientAdvertisement.getDueAddress().getProvince());
            GeoCodeAddressDto geoCodeAddressDto1=new GeoCodeAddressDto();
            geoCodeAddressDto1.setDistrict(simpleAdvertisementAddress1.getDistrict());
            geoCodeAddressDto1.setNeighborhood(simpleAdvertisementAddress1.getNeighborhood());
            geoCodeAddressDto1.setProvince(simpleAdvertisementAddress1.getProvince());
            PositionDto positionDto1= createLanLng(geoCodeAddressDto1);
            simpleAdvertisementAddress1.setLat(positionDto1.getLatitude());
            simpleAdvertisementAddress1.setLng(positionDto1.getLongitude());
            clientAdvertisement.setSimpleDueAddress(simpleAdvertisementAddress1);

        });
        clientAdvertisementRepository.saveAll(clientAdvertisementList);*/
    }

    private void loadLatlong() {
     /*  List<ClientAdvertisement> clientAdvertisementList= clientAdvertisementRepository.getAllSimple();
        clientAdvertisementList.stream().forEach((clientAdvertisement)->{
           GeoCodeAddressDto startingAddress=  GeoCodeAddressDto.builder()
                   .neighborhood(clientAdvertisement.getStartingAddress().getNeighborhood())
                   .district(clientAdvertisement.getStartingAddress().getDistrict())
                   .province(clientAdvertisement.getStartingAddress().getProvince())
                   .street(clientAdvertisement.getStartingAddress().getStreet())
                   .buildingInformation(clientAdvertisement.getStartingAddress().getBuildingInformation())
                   .fullAddress(clientAdvertisement.getStartingAddress().getFullAddress())
                   .build();

            GeoCodeAddressDto dueAddress=  GeoCodeAddressDto.builder()
                    .neighborhood(clientAdvertisement.getDueAddress().getNeighborhood())
                    .district(clientAdvertisement.getDueAddress().getDistrict())
                    .province(clientAdvertisement.getDueAddress().getProvince())
                    .street(clientAdvertisement.getDueAddress().getStreet())
                    .buildingInformation(clientAdvertisement.getDueAddress().getBuildingInformation())
                    .fullAddress(clientAdvertisement.getDueAddress().getFullAddress())
                    .build();

            PositionDto positionDueAddress=createLanLng(dueAddress);
            PositionDto positionStartingAddress=createLanLng(startingAddress);
            clientAdvertisement.getDueAddress().setLat(positionDueAddress.getLatitude());
            clientAdvertisement.getDueAddress().setLng(positionDueAddress.getLongitude());
            clientAdvertisement.getStartingAddress().setLat(positionStartingAddress.getLatitude());
            clientAdvertisement.getStartingAddress().setLng(positionStartingAddress.getLongitude());
        });
        clientAdvertisementRepository.saveAll(clientAdvertisementList);*/
    }

    private void summaryClientAdvertisementLoader() {
        List<ClientAdvertisementBid> clientAdvertisementBidList = clientAdvertisementBidRepository.findAll();
        clientAdvertisementBidList.stream().forEach((clientAdvertisementBid) -> {
            if (Optional.ofNullable(clientAdvertisementBid.getClientAdvertisement().getClientAdvertisementCode()).isPresent()) {
                clientAdvertisementBid.getSummaryAdvertisementData().setClientAdvertisementCode(
                        clientAdvertisementBid.getClientAdvertisement().getClientAdvertisementCode()

                );
            }


        });
        clientAdvertisementBidRepository.saveAll(clientAdvertisementBidList);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double calculateVAT(double value, int places) {
        return round(value * 1.18D, places);
    }

    private void advertisementBidCalculate() {
        List<ClientAdvertisementBid> clientAdvertisementBidList = clientAdvertisementBidRepository.findAll();
        clientAdvertisementBidList.forEach((clientAdvertisementBid) -> {
            clientAdvertisementBid.setBidWithVat(calculateVAT(clientAdvertisementBid.getBid(), 2));
        });
        clientAdvertisementBidRepository.saveAll(clientAdvertisementBidList);

    }

    private void accountSettingLoad() {
        List<Client> clientList = clientRepository.findAll();
        clientList.stream().forEach((client) -> {
            ClientAccountSetting clientAccountSetting = new ClientAccountSetting();
            clientAccountSetting.setCreateAdvertisementStartingAddressAutoFill(Boolean.FALSE);
            client.setAccountSetting(clientAccountSetting);
        });
        clientRepository.saveAll(clientList);
    }

    private void notificationSettingLoad() {
        List<Client> clientList = clientRepository.findAll();
        clientList.stream().forEach((client) -> {
            ClientNotificationSetting clientNotificationSetting = new ClientNotificationSetting();
            clientNotificationSetting.setNewAdvertisementBidEmailSending(Boolean.FALSE);
            clientNotificationSetting.setNewAdvertisementBidMobileSending(Boolean.FALSE);
            clientNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.FALSE);
            clientNotificationSetting.setStatusChangeAdvertisementMobileSending(Boolean.FALSE);
            client.setNotificationSetting(clientNotificationSetting);
        });
        clientRepository.saveAll(clientList);

        List<Driver> driverList = driverRepository.findAll();
        driverList.stream().forEach((driver) -> {
            DriverNotificationSetting driverNotificationSetting = new DriverNotificationSetting();
            driverNotificationSetting.setNewAdvertisementMobileSending(Boolean.FALSE);
            driverNotificationSetting.setNewAdvertisementMailSending(Boolean.FALSE);
            driverNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.FALSE);
            driverNotificationSetting.setStatusChangeAdvertisementMobileSending(Boolean.FALSE);
            driver.setNotificationSetting(driverNotificationSetting);
        });
        driverRepository.saveAll(driverList);
    }

    private void seperatorAdd() {
        List<User> userList = userRepository.findAll();
        userList.stream().forEach((user) -> {
            String areaCode = user.getPhone().substring(0, 3);
            String number = user.getPhone().substring(3);
            user.setPhone(areaCode + "~" + number);
        });
        userRepository.saveAll(userList);
    }

    private void defaultLanguage() {
        List<User> userList = userRepository.findAll();
        userList.stream().forEach((user) -> {
            user.setLanguage(Language.TURKISH);
        });

        userRepository.saveAll(userList);
    }

    private void regionAdvertisementTypeLoad() {
        List<ClientAdvertisement> clientAdvertisementList = clientAdvertisementRepository.findAll();
        clientAdvertisementList.stream().forEach((clientAdvertisement) -> {
            clientAdvertisement.setRegionAdvertisementType(RegionAdvertisementType.DOMESTIC);
        });
        clientAdvertisementRepository.saveAll(clientAdvertisementList);
    }

    private void clientAdvertisementContainerPhoneAddSeperator() {
        List<ClientAdvertisementContainer> clientAdvertisementContainerList = clientAdvertisementContainerRepository.findAll();
        clientAdvertisementContainerList.stream().forEach(clientAdvertisementContainer -> {
            if (Optional.ofNullable(clientAdvertisementContainer.getCustomsOfficerPhone()).isPresent()) {
                String areaCode = clientAdvertisementContainer.getCustomsOfficerPhone().substring(0, 3);
                String number = clientAdvertisementContainer.getCustomsOfficerPhone().substring(3);
                clientAdvertisementContainer.setCustomsOfficerPhone(areaCode + "~" + number);
            }

        });
        clientAdvertisementContainerRepository.saveAll(clientAdvertisementContainerList);
    }

    private void clientAdvertisementRecipientPhoneAddSeperator() {
        List<ClientAdvertisement> clientAdvertisementList = clientAdvertisementRepository.findAll();
        clientAdvertisementList.stream().forEach(clientAdvertisement -> {
            if (Optional.ofNullable(clientAdvertisement.getStartRecipient().getPhoneNumber()).isPresent() && clientAdvertisement.getStartRecipient().getPhoneNumber().length() > 4) {
                String areaCode = clientAdvertisement.getStartRecipient().getPhoneNumber().substring(0, 3);
                String number = clientAdvertisement.getStartRecipient().getPhoneNumber().substring(3);
                clientAdvertisement.getStartRecipient().setPhoneNumber(areaCode + "~" + number);
            }
            if (Optional.ofNullable(clientAdvertisement.getDueRecipient().getPhoneNumber()).isPresent() && clientAdvertisement.getDueRecipient().getPhoneNumber().length() > 4) {
                String areaCode = clientAdvertisement.getDueRecipient().getPhoneNumber().substring(0, 3);
                String number = clientAdvertisement.getDueRecipient().getPhoneNumber().substring(3);
                clientAdvertisement.getDueRecipient().setPhoneNumber(areaCode + "~" + number);
            }
            if (clientAdvertisement instanceof ClientAdvertisementContainer) {
                if (Optional.ofNullable(((ClientAdvertisementContainer) clientAdvertisement).getContainerRecipient().getPhoneNumber()).isPresent() && ((ClientAdvertisementContainer) clientAdvertisement).getContainerRecipient().getPhoneNumber().length() > 4) {
                    String areaCode = ((ClientAdvertisementContainer) clientAdvertisement).getContainerRecipient().getPhoneNumber().substring(0, 3);
                    String number = ((ClientAdvertisementContainer) clientAdvertisement).getContainerRecipient().getPhoneNumber().substring(3);
                    ((ClientAdvertisementContainer) clientAdvertisement).getContainerRecipient().setPhoneNumber(areaCode + "~" + number);
                }
            }
        });
        clientAdvertisementRepository.saveAll(clientAdvertisementList);
    }

    private void savedAddressPhoneAddSeperator() {
        List<SavedAddress> savedAddresses = savedAddressRepository.findAll();
        savedAddresses.stream().forEach(savedAddress -> {
            if (Optional.ofNullable(savedAddress.getPhone()).isPresent() && savedAddress.getPhone().length() > 4) {
                String areaCode = savedAddress.getPhone().substring(0, 3);
                String number = savedAddress.getPhone().substring(3);
                savedAddress.setPhone(areaCode + "~" + number);
            }
        });
        savedAddressRepository.saveAll(savedAddresses);
    }

    private void savedAddressRegionLoad() {
        List<SavedAddress> savedAddresses = savedAddressRepository.findAll();
        savedAddresses.stream().forEach(savedAddress -> {
            if (Optional.ofNullable(savedAddress.getDistrict()).isPresent()) {
                savedAddress.setRegionType(RegionType.DOMESTIC);
            } else {
                savedAddress.setRegionType(RegionType.INTERNATIONAL);
            }
        });
        savedAddressRepository.saveAll(savedAddresses);
    }

    private void atpUpdate(){
        List<ClientTransportProcess> clientTransportProcessList=clientTransportProcessRepository.findAll();
        clientTransportProcessList.stream().forEach(clientTransportProcess -> {
            if(clientTransportProcess.getTransportCode().substring(0,3).equals("ATP")){
                clientTransportProcess.setTransportCode(StringUtils.replace(clientTransportProcess.getTransportCode(),"ATP","CTP"));
            }

        });
        clientTransportProcessRepository.saveAll(clientTransportProcessList);
    }

    private void clientTypeUpdate(){
        List<Client> clientList=clientRepository.findAll();
       clientList.stream().forEach((client -> {
           if(client.getRole()==Role.ROLE_CLIENT_EMPLOYEE){
               client.setClientType(client.getBoss().getClientType()
               );
           }
       }));

       clientRepository.saveAll(clientList);
    }

    @Override
    public void run(String... args) throws Exception {


        //DEFAULT ADMIN HESABI

        Long count = userRepository.countByRole(Role.ROLE_ADMIN);

        if (count == null || count == 0) {
            DriverNotificationSetting driverNotificationSetting = new DriverNotificationSetting();
            driverNotificationSetting.setNewAdvertisementMailSending(Boolean.TRUE);
            driverNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.TRUE);

            ClientNotificationSetting clientNotificationSetting = new ClientNotificationSetting();
            clientNotificationSetting.setNewAdvertisementBidEmailSending(Boolean.TRUE);
            clientNotificationSetting.setStatusChangeAdvertisementEmailSending(Boolean.TRUE);

            String passwordAdmin = encoder.encode("JpWy2D[zfirv");
            User admin = new User();
            admin.setFirstName("Admin isim");
            admin.setLastName("Admin Soyisim");
            admin.setPhone("+90~0001231212");
            admin.setPassword(passwordAdmin);
            admin.setRole(Role.ROLE_ADMIN);
            admin.setMailConfirmed(Boolean.TRUE);
            admin.setPhoneConfirmed(Boolean.TRUE);
            userRepository.save(admin);

            String passwordAdmin2 = encoder.encode("JpWy2D[zfirv");
            User admin2 = new User();
            admin2.setFirstName("Admin Firstname");
            admin2.setLastName("Admin Lastname");
            admin2.setPhone("+90~1001231212");
            admin2.setEmail("admin@admin.com");
            admin2.setPassword(passwordAdmin2);
            admin2.setRole(Role.ROLE_ADMIN);
            admin2.setMailConfirmed(Boolean.TRUE);
            admin2.setPhoneConfirmed(Boolean.TRUE);
            userRepository.save(admin2);

            //-------------------------------------------------------------

            String passwordClient = encoder.encode("ljstclient");
            Client client = new Client();

            client.setCurrent(false);
            client.setTl(0.0);
            client.setUsd(0.0);
            client.setEuro(0.0);


            Company companyClient = new Company();
            Address addressClient = new Address();

            addressClient.setCountry("Country1");
            addressClient.setDistrict("District1");
            addressClient.setProvince("Province1");
            addressClient.setNeighborhood("Neighborhood1");
            addressClient.setFullAddress("Full Address1");

            companyClient.setCommercialTitle("commercial Title1");
            companyClient.setTaxNumber("tax number1");
            addressClient.setFullAddress("Denizli Aselsis Driver Full Adres1");
            companyClient.setAddress(addressClient);
            companyClient.setTaxAdministration("Tax Administration1");
            companyClient.setFinancialStaffFirstname("FinancialStaffFirstname1");
            companyClient.setFinancialStaffLastname("FinancialStaffLastname");
            companyClient.setFinancialStaffPhone("FinancialStaffPhone");
            companyClient.setMail("company@mail");
            companyClient.setPhone("CompanyPhone");
            companyClient.setRating(10.0);

            companyClient = companyRepository.saveAndFlush(companyClient);

            client.setCompany(companyClient);
            client.setFirstName("Client Firstname");
            client.setLastName("Client Lastname");
            client.setPhone("+90~2001231212");
            client.setEmail("client@client.com");
            client.setPassword(passwordClient);
            client.setClientType(ClientType.PERSON);
            client.setRole(Role.ROLE_CLIENT);
            client.setMailConfirmed(Boolean.TRUE);
            client.setPhoneConfirmed(Boolean.TRUE);
            client.setNotificationSetting(clientNotificationSetting);
            client.setLanguage(Language.TURKISH);
            clientRepository.save(client);

            //-------------------------------------------------------------

            String passwordDriver = encoder.encode("ljstdriver");

            Driver driver = new Driver();
            driver.setFirstName("Driver Firstname");
            driver.setLastName("Driver Lastname");
            driver.setPhone("+90~3001231212");
            driver.setPassword(passwordDriver);
            driver.setCitizenId("12345678912");
            driver.setEmail("driver@driver.com");
            driver.setStatus(DriverStatus.ACCEPTED);
            driver.setRole(Role.ROLE_DRIVER);
            driver.setDriverTitle(DriverTitle.BOSS);
            driver.setMailConfirmed(Boolean.TRUE);
            driver.setPhoneConfirmed(Boolean.TRUE);
            driver.setNotificationSetting(driverNotificationSetting);

            Company company = new Company();
            Address address = new Address();
            address.setCountry("Country1");
            address.setDistrict("District1");
            address.setProvince("Province1");
            address.setNeighborhood("Neighborhood1");
            address.setFullAddress("Full Address1");

            BankInformation bankInformation = new BankInformation();
            bankInformation.setAccountName("AccountName1");
            bankInformation.setBankName("BankName1");
            bankInformation.setIban("Iban1");
            bankInformation.setBranch("Branch1");
            bankInformation.setAccountNumber("AccountNumber1");
            bankInformation = bankInformationRepository.save(bankInformation);

            company.setCommercialTitle("commercial Title1");
            company.setTaxNumber("tax number1");
            address.setFullAddress("Denizli Aselsis Driver Full Adres1");
            company.setAddress(address);
            company.setTaxAdministration("Tax Administration1");
            company.setBankInformation(bankInformation);
            company.setFinancialStaffFirstname("FinancialStaffFirstname1");
            company.setFinancialStaffLastname("FinancialStaffLastname");
            company.setFinancialStaffPhone("FinancialStaffPhone");
            company.setMail("company@mail");
            company.setPhone("CompanyPhone");
            company.setRating(10.0);
            company = companyRepository.saveAndFlush(company);

            driver.setCompany(company);
            driverRepository.saveAndFlush(driver);

            createInitialUserTwo();

            //INITIALIZE DYNAMIC DATA
            createDynamicAdminPanelData();

        }
        // createSimpleAdvertisementAddress();
        // updateClientAdvertisementCode();
        // loadLatlong();
        //summaryClientAdvertisementLoader();
        // advertisementBidCalculate();
        // accountSettingLoad();
        //notificationSettingLoad();
        //TODO YURT DIŞINDA ÇALIŞTIR

        // seperatorAdd();
        // defaultLanguage();
        //regionAdvertisementTypeLoad();
        // clientAdvertisementContainerPhoneAddSeperator();
         //clientAdvertisementRecipientPhoneAddSeperator();
        //savedAddressPhoneAddSeperator();
        // savedAddressRegionLoad();
        //atpUpdate();

       // clientTypeUpdate();
    }

}
