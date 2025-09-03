package com.example.product.shared.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.product.api.v1.dto.jsonapi.UpdateProductRequest;

@Component
public class UpdateProductValidator implements Validator {

    private final ValidationsConfig validationsConfig;

    public UpdateProductValidator (ValidationsConfig validationsConfig) {
        this.validationsConfig = validationsConfig;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateProductRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateProductRequest request = (UpdateProductRequest) target;
        UpdateProductRequest.UpdateProductData attr = request.data();

        if (attr.name() != null && attr.name().length() > validationsConfig.getNameMaxLength()) {
            errors.rejectValue("data.attributes.name", "name.length", "Product name cannot exceed " + validationsConfig.getNameMaxLength() + " characters.");
        }

        if (attr.description() != null && attr.description() != null && attr.description().length() > validationsConfig.getDescriptionMaxLength()) {
            errors.rejectValue("data.attributes.description", "description.length", "Description cannot exceed " + validationsConfig.getDescriptionMaxLength() + " characters.");
        }

        if (attr.price() != null && attr.price().compareTo(validationsConfig.getPriceMinValue()) < 0) {
            errors.rejectValue("data.attributes.price", "price.min", "Price must be at least " + validationsConfig.getPriceMinValue());
        }
        if (attr.price() != null && attr.price().compareTo(validationsConfig.getPriceMaxValue()) > 0) {
            errors.rejectValue("data.attributes.price", "price.max", "Price value is too high, max is " + validationsConfig.getPriceMaxValue());
        }
    }
}
