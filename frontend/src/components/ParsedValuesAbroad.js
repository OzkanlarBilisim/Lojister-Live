export const ParsedClientAbroud = (data) => {   
    let current = false;
    switch (data.status) {

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
        case "APPROVED":
            return {
                buttonText:  current? "Ödemeyi Yap": "Sigorta Seç",
                buttonShown: true,
                title: current? "Ödeme bekliyor!": "Sigorta bekliyor!",
                description: current? "Ödeme bekliyor!": "Sigorta bekliyor!",
                alertDescription: current? "Sürecin başlaması için ödeme yapmalısınız!": "Sürecin başlaması için sigorta seçmelisiniz!",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 25,
                step: 0,
            };
        case "TEMPORARY_METHOD":
            return {
                buttonText: "Ödemeyi Yap",
                buttonShown: true,
                title: "Ödeme bekliyor!",
                description: "Ödeme bekliyor!",
                alertDescription: "Sürecin tamamlanması için ödeme yapmalısınız!",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 75,
                step: 0,
            };
        case "TEMPORARY_METHOD2":
            return {
                buttonText: "Ödemeyi Yap",
                buttonShown: false,
                title: "Ödeme onay sürecinde!",
                description: "Ödeme onay sürecinde!",
                alertDescription: "Yüklediğiniz dekont inceleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 0,
            };

        case "PAYMENT_SUCCESSFUL":
            return {
                buttonShown: false,
                title: "Taşıyıcıdan bilgi bekleniyor!",
                description: "Taşıyıcı tarafından bilgi girmesi bekleniyor.",
                alertDescription: "Taşıyıcı tarafından bilgi girmesi bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 50,
                step: 0,
            };
        case "SHIPSMENTINFO":
            return {
                buttonShown: false,
                title: "Yükleme bekleniyor!",
                description: "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                alertDescription: "Yükün taşıyıcı tarafından teslim alınması bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 50,
                step: 0,
            };
        case "WAITING_FOR_TRANSPORT":
            return {
                buttonText: "Teslim Et",
                buttonShown: true,
                title: "Yükün teslim etemeniz bekleniyor!",
                description: "Yükü teslim etemeniz bekleniyor.",
                alertDescription: "Yükü teslim etemeniz bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 0,
            };
    
        case "TRANSPORT":
            return {
                buttonShown: false,
                title: "Yük taşıma sürecinde!",
                description: "Yük taşıma sürecinde.",
                alertDescription: "Yük taşıma sürecinde.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 75,
                step: 1,
            };
        case "COMPLETED":
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
        case "RATING":
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
        case "HIDDEN":
            return {
                buttonShown: false,
                title: "Reddedildi!",
                description: "İlan reddedildi.",
                alertDescription: "İlan reddedildi.",
                color: "red",
                bg: "var(--red-light)",
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

export const ParsedDriverAbroud = (data) => {
    if(typeof data === "object"){
        var status = data.status;
    }else{
        var status = data;
    }
    switch (status) {
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
        case "APPROVED":
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
                buttonText: "Bigi Gir",
                buttonShown: true,
                title: "Bilgi girin!",
                description: "Taşıma bilgilerini girmeniz bekleniyor.",
                alertDescription: "Taşıma bilgilerini girmeniz bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 0,
            };
        case "SHIPSMENTINFO":
            return {
                buttonText: "Teslim Al",
                buttonShown: true,
                title: "Yükün teslim alınması bekleniyor!",
                description: "Yükü teslim almanz bekleniyor.",
                alertDescription: "Yükü teslim almanız bekleniyor.",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 50,
                step: 0,
            };
        case "WAITING_FOR_TRANSPORT":
            return {
                buttonShown: false,
                title: "Yükün teslim edilmesi bekleniyor!",
                description: "Yükün teslim edilmesi bekleniyor!",
                alertDescription: "Yükün teslim edilmesi bekleniyor.",
                color: "blue",
                bg: "var(--blue-light)",
                percent: 50,
                step: 0,
            };
        case "TRANSPORT":
            return {
                buttonText: "Teslim Et",
                buttonShown: true,
                title: "Yük taşıma sürecinde",
                description: "Yükü teslim etmeniz gerekiyor!",
                alertDescription: "Yükü teslim etmeniz gerekiyor!",
                color: "orange",
                bg: "var(--orange-light)",
                percent: 75,
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
        case "RATING":
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
        case "TEMPORARY_METHOD":
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
        case "TEMPORARY_METHOD2":
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
        case "HIDDEN":
            return {
                buttonShown: false,
                title: "Reddedildi!",
                description: "İlan reddedildi.",
                alertDescription: "İlan reddedildi.",
                color: "red",
                bg: "var(--red-light)",
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