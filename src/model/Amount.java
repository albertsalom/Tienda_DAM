package model;

import java.text.DecimalFormat;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

public class Amount {
	private double value;	
	private String currency="€";
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public Amount () {
		currency = "€";
	};
	public Amount(double value) {
		super();
		this.value = value;
	}

	@XmlValue
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@XmlAttribute(name="currency")
	public String getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return df.format(value) + currency;
	}	
}
