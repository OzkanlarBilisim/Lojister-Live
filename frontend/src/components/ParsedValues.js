export const ParsedClient = (data) => {
    let isContainer = data?.transport?.clientAdvertisementType === "CONTAINER";
    let isExport = data?.transport?.tradeType === "EXPORT";
    let vgmUploaded = data?.transportDocuments?.some(v => v.documentType === "VGM");

    switch (typeof data === "object" ? data?.status : data) {
        case "WAITING":
            return {
                buttonShown: false,
                title: "Teklif bekliyor!",
                description: "İlanınız teklif bekliyor.",
                alertDescription: "Aşağıdan gelen teklifleri değerlendirerek taşıma sürecini başlatabilirsiniz.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 0,
                step: 0,
            };
        case "BID_GIVEN":
            return {
                buttonShown: false,
                title: "Teklif alındı.",
                description: "Teklif alındı.",
                alertDescription: "Aşağıdan gelen teklifleri değerlendirebilirsiniz.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 0,
                step: 0,
            };
        case "BID_APPROVED":
            return {
                buttonText: "Ödemeyi Yap",
                buttonShown: true,
                title: "Ödeme bekliyor!",
                description: "Ödeme bekliyor!",
                alertDescription: "Sürecin başlaması için ödeme yapmalısınız!",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };

        case "PAYMENT_SUCCESSFUL":
            return {
                buttonShown: false,
                title: "Yükleme bekleniyor!",
                description: "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                alertDescription: "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };
        case "ASSIGNED_VEHICLE":
            return {
                buttonText: "Belge Yükle",
                buttonShown: typeof data === "object" && isContainer && isExport ? true : false,
                title: typeof data === "object" && isContainer && isExport ? "Föy belgesi bekleniyor" : "Yükleme bekleniyor!",
                description: typeof data === "object" && isContainer && isExport ? "Föy belgesi yüklemeniz bekleniyor" : "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                alertDescription: typeof data === "object" && isContainer && isExport ? "Föy belgesi yüklemeniz bekleniyor" : "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                color: typeof data === "object" && isContainer && isExport ? "orange" : "blue",
                bg: typeof data === "object" && isContainer && isExport ? "var(--orange-light)" : "var(--blue-light)",
                percent: 25,
                step: 0,
            };
        case "UPLOADED_DOCUMENT":
            return {
                buttonShown: false,
                title: vgmUploaded ? "Teslimat bekleniyor!" : "Konteyner bekleniyor!",
                description: vgmUploaded ? "Taşıyıcının yükü teslim etmesi bekleniyor!" : "Taşıyıcının konteyneri alması bekleniyor.",
                alertDescription: vgmUploaded ? "Taşıyıcının yükü teslim etmesi bekleniyor!" : "Taşıyıcının konteyneri alması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: vgmUploaded ? 75 : 25,
                step: vgmUploaded ? 1 : 0,
            };
        case "CONTAINER_RECEIVED":
            return {
                buttonShown: false,
                title: "Yükleme bekleniyor!",
                description: "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                alertDescription: "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };
        case "STARTING_TRANSPORT":
            return {
                buttonText: "Onayla",
                buttonShown: true,
                title: "Yüklemeyi onaylayın!",
                description: "Yükün teslim alındığını onaylayın.",
                alertDescription: "Yükün teslim alındığını onaylayın.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "CARGO_ON_THE_WAY":
            return {
                buttonShown: false,
                title: "Yolda!",
                description: "Teslim edilmek üzere yolda.",
                alertDescription: "Teslim edilmek üzere yolda.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 50,
                step: 1,
            };
        case "CARGO_DELIVERED":
            return {
                buttonShown: false,
                title: "Konteyner yolda!",
                description: "Konteyner iade edilmek üzere yolda.",
                alertDescription: "Konteyner iade edilmek üzere yolda.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 50,
                step: 1,
            };
        case "ENDING_TRANSPORT":
            return {
                buttonText: "Onayla",
                buttonShown: true,
                title: "Teslimatı onaylayın!",
                description: "Yükün teslim edildiğini onaylayın.",
                alertDescription: "Yükün teslim edildiğini onaylayın.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 75,
                step: 1,
            };
        case "WAITING_WAYBILL":
            return {
                buttonShown: false,
                title: "İrsaliye bekliyor!",
                description: "Teslimat için irsaliye bekleniyor.",
                alertDescription: "Teslimat için irsaliye bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };

        case "WAYBILL_DENIED":
            return {
                buttonShown: false,
                title: "İrsaliye bekliyor!",
                description: "Teslimat için irsaliye bekleniyor.",
                alertDescription: "Teslimat için irsaliye bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };
        case "CARGO_COULD_NOT_BE_DELIVERED":
            return {
                buttonShown: false,
                title: "Teslim edilemedi!",
                description: "Taşıma tamamlanamadı.",
                alertDescription: "Taşıma tamamlanamadı.",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 1,
            };
        case "CLIENT_CARGO_PROBLEM":
            return {
                buttonShown: false,
                title: "Kargo problemi!",
                description: "Kargo problemi!",
                alertDescription: "Kargo problemi.",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 1,
            };
        case "COMPLETED":
            return {
                buttonText: "Süreci Değerlendir",
                buttonShown: true,
                title: "Tamamlandı!",
                description: "Taşıma tamamlandı.",
                alertDescription: "Taşıma tamamlandı.",
                color: "green",
                bg: "var(--green-light)",
                percent: 100,
                step: 2,
            };
        case "FINISHED":
            return {
                buttonText: "Süreci Değerlendir",
                buttonShown: false,
                title: "Tamamlandı!",
                description: "Taşıma tamamlandı.",
                alertDescription: "Taşıma tamamlandı.",
                color: "green",
                bg: "var(--green-light)",
                percent: 100,
                step: 2,
            };
        case "HIDDEN":
            return {
                buttonShown: false,
                title: "Gizlendi!",
                description: "İlan gizlendi.",
                alertDescription: "İlan gizlendi.",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        default:
            return {
                buttonShown: false,
                title: "Bilinmiyor!",
                description: "İlan durumu bilinmiyor.",
                alertDescription: "İlan durumu bilinmiyor.",
                percent: 0,
                step: 0,
                color: "dark",
                bg: "var(--dark-light)",
            };
    }
};


export const ParsedDriver = (data) => {
    let isContainer = data?.transport?.clientAdvertisementType === "CONTAINER";
    let isExport = data?.transport?.tradeType === "EXPORT";
    let vgmUploaded = data?.transportDocuments?.some(v => v.documentType === "VGM");

    switch (typeof data === "object" ? data?.status : data) {
        case "WAITING":
            return {
                buttonShown: false,
                title: "Değerlendiriliyor!",
                description: "Teklifiniz değerlendiriliyor.",
                alertDescription: "Teklifiniz ilan sahibi tarafından değerlendiriliyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 0,
                step: 0,
            };
        case "TIMEOUT":
            return {
                buttonShown: false,
                title: "Teklif süresi geçti!",
                description: "Teklif süresi geçtiğinden dolayı iptal edilmiştir.",
                alertDescription: "Teklif süresi geçtiğinden dolayı iptal edilmiştir.",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        case "BID_GIVEN":
            return {
                buttonShown: false,
                title: "Teklif alındı!",
                description: "Gelen teklifleri değerlendirebilirsiniz.",
                alertDescription: "Gelen teklifleri değerlendirebilirsiniz.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 0,
                step: 0,
            };
        case "BID_APPROVED":
            return {
                buttonShown: false,
                title: "Ödeme Bekleniyor",
                description: "İlan sahibinin ödeme yapması bekleniyor.",
                alertDescription: "İlan sahibinin ödeme yapması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };

        case "PAYMENT_SUCCESSFUL":
            return {
                buttonText: "Araç Ata",
                buttonShown: true,
                title: "Araç ataması bekleniyor!",
                description: "Taşıma için araç atamanız bekleniyor.",
                alertDescription: "Taşıma için araç atamanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "ASSIGN_VEHICLE":
            return {
                buttonText: "Teslim Al",
                buttonShown: true,
                title: "Yükleme bekleniyor!",
                description: "Yükü teslim almanız bekleniyor.",
                alertDescription: "Yükü teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "ASSIGNED_VEHICLE":
            return {
                buttonText: "Teslim Al",
                buttonShown: typeof data === "object" && isContainer && isExport ? false : true,
                title: typeof data === "object" && isContainer && isExport ? "Föy bekleniyor!" : "Yükleme bekleniyor!",
                description: typeof data === "object" && isContainer && isExport ? "Föy belgesinin yük sahibi tarafından yüklenmesi bekleniyor." : "Yükü teslim almanız bekleniyor.",
                alertDescription: typeof data === "object" && isContainer && isExport ? "Föy belgesinin yük sahibi tarafından yüklenmesi bekleniyor." : "Yükü teslim almanız bekleniyor.",
                color: typeof data === "object" && isContainer && isExport ? "blue" : "orange",
                bg: typeof data === "object" && isContainer && isExport ? "var(--blue-light)" : "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "UPLOADED_DOCUMENT":
            return {
                buttonText: vgmUploaded ? "Teslim Et" : "Teslim Al",
                buttonShown: true,
                title: vgmUploaded ? "Teslimat bekleniyor!" : "Konteyner bekleniyor!",
                description: vgmUploaded ? "Yükü teslim etmeniz bekleniyor!" : "Konteyneri teslim almanız bekleniyor.",
                alertDescription: vgmUploaded ? "Yükü teslim etmeniz bekleniyor!" : "Konteyneri teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: vgmUploaded ? 75 : 25,
                step: vgmUploaded ? 1 : 0,
            };
        case "CONTAINER_RECEIVED":
            return {
                buttonText: "Teslim Al",
                buttonShown: true,
                title: "Yükleme bekleniyor!",
                description: "Yükü teslim almanız bekleniyor.",
                alertDescription: "Yükü teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "STARTING_TRANSPORT":
            return {
                buttonShown: false,
                title: "Onay bekleniyor!",
                description: "Yükün teslim alınma onayı bekleniyor.",
                alertDescription: "Yükün teslim alınma onayı bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };
        case "CARGO_ON_THE_WAY":
            return {
                buttonText: typeof data === "object" && isContainer && isExport ? "Belge Yükle" : "Teslim Et",
                buttonShown: true,
                title: typeof data === "object" && isContainer && isExport ? "VGM belgesi bekleniyor" : "Yolda!",
                description: typeof data === "object" && isContainer && isExport ? "VGM belgesi yüklemeniz bekleniyor" : "Yükü teslim etmeniz bekleniyor.",
                alertDescription: typeof data === "object" && isContainer && isExport ? "VGM belgesi yüklemeniz bekleniyor" : "Yükü teslim etmeniz bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 1,
            };
        case "CARGO_DELIVERED":
            return {
                buttonText: "Teslim Et",
                buttonShown: true,
                title: "Konteyner yolda!",
                description: "Konteyneri iade etmeniz bekleniyor.",
                alertDescription: "Konteyneri iade etmeniz bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 1,
            };
        case "ENDING_TRANSPORT":
            return {
                buttonShown: false,
                title: "Onay bekleniyor!",
                description: "Yükün teslim edilme onayı bekleniyor.",
                alertDescription: "Yükün teslim edilme onayı bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };
        case "WAITING_WAYBILL":
            return {
                buttonShown: false,
                title: "İrsaliye onayı bekleniyor!",
                description: "İrsaliyenin yönetici tarafından onaylanması bekleniyor.",
                alertDescription: "İrsaliyenin yönetici tarafından onaylanması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };

        case "WAYBILL_DENIED":
            return {
                buttonText: "Yeniden Yükle",
                buttonShown: true,
                title: "İrsaliye reddedildi!",
                description: "Yeniden irsaliye yüklemesi yapılması gerekiyor.",
                alertDescription: "Yeniden irsaliye yüklemesi yapılması gerekiyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 75,
                step: 1,
            };
        case "CARGO_COULD_NOT_BE_DELIVERED":
            return {
                buttonShown: false,
                title: "Teslim edilemedi!",
                description: "Taşıma tamamlanamadı.",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 1,
            };
        case "CLIENT_CARGO_PROBLEM":
            return {
                buttonShown: false,
                title: "Kargo problemi!",
                description: "Kargo problemi.",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 1,
            };
        case "COMPLETED":
            return {
                buttonShown: false,
                title: "Tamamlandı!",
                description: "Taşıma tamamlandı.",
                alertDescription: "Taşıma tamamlandı.",
                color: "green",
                bg: "var(--green-light)",
                percent: 100,
                step: 2,
            };
        case "HIDDEN":
            return {
                buttonShown: false,
                title: "Gizlendi!",
                description: "İlan gizlendi.",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        case "CANCELLED":
            return {
                buttonText: "Tekrar Teklif Ver",
                buttonShown: true,
                title: "Teklif iptal edildi!",
                alertDescription: "Verdiğiniz teklifi iptal ettiniz.",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 0,
            };
        case "DENIED":
            return {
                buttonText: "Tekrar Teklif Ver",
                buttonShown: true,
                title: "Teklif reddedildi!",
                alertDescription: "Verilen teklif reddedildi!",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 0,
            };
        case "APPROVED":
            return {
                buttonShown: false,
                title: "Ödeme bekliyor",
                alertDescription: "İlan sahibinin ödeme yapması bekleniyor!",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };
        case "AD_CLOSED":
            return {
                buttonShown: false,
                title: "İlan kapatıldı!",
                alertDescription: "İlan kapatıldı.",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        default:
            return {
                buttonShown: false,
                title: "Bilinmiyor!",
                description: "İlan durumu bilinmiyor.",
                percent: 0,
                step: 0,
                color: "dark",
                bg: "var(--dark-light)",
            };
    }
};


export const ParsedOffer = (data) => {
    let isContainer = data?.transport?.clientAdvertisementType === "CONTAINER";
    let isExport = data?.transport?.tradeType === "EXPORT";
    let vgmUploaded = data?.transportDocuments?.some(v => v.documentType === "VGM");

    switch (data) {
        case "WAITING":
            return {
                buttonShown: false,
                title: "Değerlendiriliyor!",
                description: "Teklifiniz değerlendiriliyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 0,
                step: 0,
            };
        case "TIMEOUT":
            return {
                buttonShown: false,
                title: "Teklif süresi geçti!",
                description: "Teklif süresi geçtiğinden dolayı iptal edilmiştir!",
                alertDescription: "Teklif süresi geçtiğinden dolayı iptal edilmiştir!",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        case "BID_GIVEN":
            return {
                buttonShown: false,
                title: "Değerlendiriliyor!",
                description: "Teklifiniz değerlendiriliyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 0,
                step: 0,
            };
        case "BID_APPROVED":
            return {
                buttonShown: false,
                title: "Ödeme Bekleniyor",
                description: "İlan sahibinin ödeme yapması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };

        case "PAYMENT_SUCCESSFUL":
            return {
                buttonText: "Araç Ata",
                buttonShown: true,
                title: "Araç ataması bekleniyor!",
                description: "Taşıma için araç atamanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "ASSIGN_VEHICLE":
            return {
                buttonText: "Teslim Al",
                buttonShown: true,
                title: "Yükleme bekleniyor!",
                description: "Yükü teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "ASSIGNED_VEHICLE":
            return {
                buttonText: "Teslim Al",
                buttonShown: true,
                title: "Yükleme bekleniyor!",
                description: "Yükü teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "UPLOADED_DOCUMENT":
            return {
                buttonText: vgmUploaded ? "Teslim Et" : "Teslim Al",
                buttonShown: true,
                title: vgmUploaded ? "Teslimat bekleniyor!" : "Konteyner bekleniyor!",
                description: vgmUploaded ? "Yükü teslim etmeniz bekleniyor!" : "Konteyneri teslim almanız bekleniyor.",
                alertDescription: vgmUploaded ? "Yükü teslim etmeniz bekleniyor!" : "Konteyneri teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: vgmUploaded ? 75 : 25,
                step: vgmUploaded ? 1 : 0,
            };
        case "STARTING_TRANSPORT":
            return {
                buttonShown: false,
                title: "Onay bekleniyor!",
                description: "Yükün teslim alınma onayı bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 25,
                step: 0,
            };
        case "CARGO_ON_THE_WAY":
            return {
                buttonText: typeof data === "object" && isContainer && isExport ? "Belge Yükle" : "Teslim Et",
                buttonShown: true,
                title: typeof data === "object" && isContainer && isExport ? "VGM belgesi bekleniyor" : "Yolda!",
                description: typeof data === "object" && isContainer && isExport ? "VGM belgesi yüklemeniz bekleniyor" : "Yükü teslim etmeniz bekleniyor.",
                alertDescription: typeof data === "object" && isContainer && isExport ? "VGM belgesi yüklemeniz bekleniyor" : "Yükü teslim etmeniz bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 1,
            };
        case "CARGO_DELIVERED":
            return {
                buttonText: "Teslim Et",
                buttonShown: true,
                title: "Konteyner yolda!",
                description: "Konteyneri iade etmeniz bekleniyor.",
                alertDescription: "Konteyneri iade etmeniz bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 1,
            };
        case "ENDING_TRANSPORT":
            return {
                buttonShown: false,
                title: "Onay bekleniyor!",
                description: "Yükün teslim edilme onayı bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };
        case "WAITING_WAYBILL":
            return {
                buttonShown: false,
                title: "İrsaliye onayı bekleniyor!",
                description: "İrsaliyenin yönetici tarafından onaylanması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };

        case "WAYBILL_DENIED":
            return {
                buttonText: "Yeniden Yükle",
                buttonShown: true,
                title: "İrsaliye reddedildi!",
                description: "Yeniden irsaliye yüklemesi yapılması gerekiyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 75,
                step: 1,
            };
        case "CARGO_COULD_NOT_BE_DELIVERED":
            return {
                buttonShown: false,
                title: "Teslim edilemedi!",
                description: "Taşıma tamamlanamadı.",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 1,
            };
        case "CLIENT_CARGO_PROBLEM":
            return {
                buttonShown: false,
                title: "Kargo problemi!",
                description: "Kargo problemi!",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 1,
            };
        case "COMPLETED":
            return {
                buttonShown: false,
                title: "Tamamlandı!",
                description: "Taşıma tamamlandı.",
                color: "green",
                bg: "var(--green-light)",
                percent: 100,
                step: 2,
            };
        case "HIDDEN":
            return {
                buttonShown: false,
                title: "Gizlendi!",
                description: "İlan gizlendi.",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        case "CANCELLED":
            return {
                buttonText: "Tekrar Teklif Ver",
                buttonShown: true,
                title: "Teklif iptal edildi!",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 0,
            };
        case "DENIED":
            return {
                buttonText: "Tekrar Teklif Ver",
                buttonShown: true,
                title: "Teklif reddedildi!",
                color: "red",
                bg: "var(--red-light)",
                percent: 0,
                step: 0,
            };
        case "AD_CLOSED":
            return {
                buttonShown: false,
                title: "İlan kapatıldı!",
                color: "dark",
                bg: "var(--dark-light)",
                percent: 0,
                step: 0,
            };
        default:
            return {
                buttonShown: false,
                title: "Bilinmiyor!",
                percent: 0,
                step: 0,
                color: "dark",
                bg: "var(--dark-light)",
            };
    }
};