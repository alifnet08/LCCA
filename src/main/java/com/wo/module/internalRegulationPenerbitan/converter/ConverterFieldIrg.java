package com.wo.module.internalRegulationPenerbitan.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("converterFieldIrg")
public class ConverterFieldIrg implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return value.split(";");
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		
		String[] stringSplit = (String[]) value;
		StringBuilder sb = new StringBuilder();
		
		for (String string : stringSplit) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(string);
		}
		
		return sb.toString();
	}

}
