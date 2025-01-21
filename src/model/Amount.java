package model;

import java.text.DecimalFormat;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "amount")  // Define el nombre del nodo raíz para Amount
public class Amount {

    private double value;
    private String currency = "€";

    private static final DecimalFormat df = new DecimalFormat("0.00");

    // Constructor necesario para JAXB
    public Amount() {}

    public Amount(double value) {
        super();
        this.value = value;
    }

    @XmlValue  // Esta anotación indica que este es el valor del nodo (el número)
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @XmlAttribute(name = "currency")  // Mapea el atributo "currency" en el XML
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

