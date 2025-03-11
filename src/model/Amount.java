package model;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.DecimalFormat;

@XmlRootElement(name = "amount")
@Embeddable
public class Amount implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Transient 
    private double value;

    @Transient 
    private String currency = "€";

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Amount() {}

    public Amount(double value) {
        this.value = value;
    }

    @XmlElement
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @XmlElement
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return df.format(value) + currency;
    }
}