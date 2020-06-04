package nl.brandfonds.Brandfonds.model.responses;

public class StripesMonth {
    private String date;
    private Integer stripeamount;

    public StripesMonth(String date, Integer stripeamount) {
        this.date = date;
        this.stripeamount = stripeamount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStripeamount() {
        return stripeamount;
    }

    public void setStripeamount(Integer stripeamount) {
        this.stripeamount = stripeamount;
    }
}
