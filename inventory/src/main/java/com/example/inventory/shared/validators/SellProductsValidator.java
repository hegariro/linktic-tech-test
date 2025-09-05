package com.example.inventory.shared.validators;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.inventory.api.v1.dto.SellItemAttribs;
import com.example.inventory.api.v1.dto.SellProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.SellProductsJsonApiRequest;

@Component
public class SellProductsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SellProductsJsonApiRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SellProductsJsonApiRequest request = (SellProductsJsonApiRequest) target;
        SellProductsJsonApiAttribs attribs = request.data().attributes();
        List<SellItemAttribs> items = attribs.items();
        for (int idx = 0; idx < items.size(); idx++) {
            SellItemAttribs item = items.get(idx);

            if (item.productId().length() != 36) {
                errors.rejectValue("data.attributes.items[" + idx + "].productId", "invalid", "El productId no es un UUID valido");
            }

            if (item.quantity() <= 0 || item.quantity() > 100) {
                errors.rejectValue("data.attributes.items[" + idx + "].quantity", "invalid", "El quantity no esta en el rango valido");
            }

            if (item.subtotal().compareTo((BigDecimal.ZERO)) < 0) {
                errors.rejectValue("data.attributes.items[" + idx + "].subtotal", "invalid", "El subtotal debe ser un número positivo");
            }
        }
    }
}
