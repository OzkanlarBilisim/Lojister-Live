package com.lojister.model.enums;


public enum ClientAdvertisementType {
    PARTIAL(Values.PARTIAL),
    FTL(Values.FTL),

    CONTAINER(Values.CONTAINER);

    private ClientAdvertisementType(String value) {
        if (!this.name().equals(value))
            throw new IllegalArgumentException("Incorrect use of AdvertisementType");
    }
    public static class Values {
        public static final String PARTIAL= "PARTIAL";
        public static final String FTL= "FTL";
        public static final String CONTAINER= "CONTAINER";
    }
}
