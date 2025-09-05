package com.example.inventory.shared.validators;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.inventory.api.v1.dto.BuyItemAttribs;
import com.example.inventory.api.v1.dto.BuyProductsJsonApiAttribs;
import com.example.inventory.api.v1.dto.BuyProductsJsonApiRequest;

@Component
public class BuyProductsValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BuyProductsJsonApiRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BuyProductsJsonApiRequest request = (BuyProductsJsonApiRequest) target;
        BuyProductsJsonApiAttribs attribs = request.data().attributes();
        List<BuyItemAttribs> items = attribs.items();
        for (int idx = 0; idx < items.size(); idx++) {
            BuyItemAttribs item = items.get(idx);

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
