package com.powerkickstkd.my_application_backend.transaction.constants;

public enum TransactionMonth {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    // Order of our months
    private final int order;

    TransactionMonth(int order) {
        this.order = order;
    }

    public int getOrder() {
        return  this.order;
    }
}
