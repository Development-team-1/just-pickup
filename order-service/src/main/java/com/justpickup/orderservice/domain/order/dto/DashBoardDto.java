package com.justpickup.orderservice.domain.order.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashBoardDto {

    //일일 판매금액
    private Long salesAmount=0L;
    private BestSellItem bestSellItem;
    List<SellAmountAWeek> sellAmountAWeeks;

    public static  DashBoardDto of(List<OrderPrice> orderPrices , DashBoardDto.BestSellItem bestSellItem , List<SellAmountAWeek> sellAmountAWeeks){
        DashBoardDto dashBoardDto = new DashBoardDto();
        orderPrices.forEach(orderPrice -> dashBoardDto.salesAmount+=orderPrice.getOrderPrice());
        dashBoardDto.bestSellItem = bestSellItem;
        dashBoardDto.sellAmountAWeeks = sellAmountAWeeks;

        return dashBoardDto;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderPrice{
        Long orderPrice;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BestSellItem{
        Long itemId;
        String itemName;
        Long sumCounts;

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellAmountAWeek{
        Object sellDate;
        Long sellAmount;
    }
}
