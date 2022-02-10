package com.justpickup.orderservice.domain.order.validator;

import com.justpickup.orderservice.domain.order.dto.PrevOrderSearch;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class PrevOrderSearchValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PrevOrderSearch.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PrevOrderSearch search = (PrevOrderSearch) target;

        LocalDate startDate = search.getStartDate();
        LocalDate endDate = search.getEndDate();
        if (startDate.isAfter(endDate)) {
            errors.rejectValue("startDate", "isAfter", "시작일은 종료일보다 클 수 없습니다.");
        }
    }
}
