package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.MenuItemService;
import kr.co.fastcampus.eatgo.domain.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MenuItemController {

    @Autowired
    private MenuItemController menuItemService;

    @PatchMapping("/restaurants/{restaurantId}/menuitems")
    public String bulkUpate(
            @PathVariable("restaurantId") Long restaurantId,
            @RequestBody List<MenuItem> menuItems
    ) {
        menuItemService.bulkUpate(restaurantId, menuItems);

        return "";
    }

}

