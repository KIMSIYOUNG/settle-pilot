package in.woowa.pilot.batch.query;

public class ItemReaderQueryString {

    public static String findOrderAggregationByOwnerAndPayment() {
        return "select new jpql.dto.OrderAggregationByOwnerDto(" +
                "o.owner as owner, " +
                "od.paymentType as paymentType, " +
                "od.paymentOption as paymentOption, " +
                "sum(od.amount) as totalAmount" +
                ") " +
                "from Order o " +
                "inner join o.owner ow " +
                "inner join o.orderDetails.orderDetails od " +
                "where o.status = 'ACTIVE' " +
                "and " +
                "o.orderDateTime >=:start " +
                "and " +
                "o.orderDateTime <:end " +
                "group by ow.id, od.paymentType, od.paymentOption";
    }

    public static String findOrderAggregationSumByPayment() {
        return "select new jpql.dto.OrderAggregationSumDto(" +
                "sum(op.totalAmount) as totalAmount, " +
                "op.paymentType as paymentType, " +
                "op.paymentOption as paymentOption " +
                ") " +
                "from OrderPaymentAggregation op " +
                "where op.criteriaDate = :criteriaDate " +
                "group by op.paymentType, op.paymentOption ";
    }

    public static String findActiveOwnerBySettleType() {
        return "select o " +
                "from Owner o " +
                "where o.status = 'ACTIVE' " +
                "and " +
                "o.settleType =:settleType ";
    }

    public static String findSettleByTypeAndDateTime() {
        return "select new jpql.dto.SettleSumDto(" +
                "coalesce(sum(s.amount), 0) as totalAmount " +
                ") " +
                "from Settle s " +
                "inner join s.owner ow " +
                "where s.settleType=:settleType " +
                "and ow.settleType=:settleType " +
                "and s.transactionStartAt >=:start " +
                "and s.transactionEndAt <= :end";
    }
}
