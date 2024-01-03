package com.lab.dev.shawn.api.admin.inventory.controller;

import com.lab.dev.shawn.api.base.dto.ApiResponseBody;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.admin.inventory.service.InventoryService;
import com.lab.dev.shawn.api.admin.inventory.vo.InventoryRequestVO;
import com.lab.dev.shawn.api.admin.inventory.vo.InventoryResponseVO;
import com.lab.dev.shawn.api.admin.inventory.vo.UpdateQuantityRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/inventory")
public class InventoryController {
    private InventoryService inventoryService;

    @Autowired
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseBody> list(int page, int limit, Long concertId) {
        Page<InventoryResponseVO> tickets = inventoryService.findByFilter(page, limit, concertId);
        ApiResponseBody body = new ApiResponseBody(tickets);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseBody> create(@RequestBody InventoryRequestVO requestVO) throws BaseException {
        inventoryService.create(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/update_quantity")
    public ResponseEntity<ApiResponseBody> update(@RequestBody UpdateQuantityRequestVO requestVO) throws BaseException {
        inventoryService.updateQuantity(requestVO);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/active/{id}")
    public ResponseEntity<ApiResponseBody> active(@PathVariable("id") String id) throws BaseException {
        inventoryService.active(id);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/inactive/{id}")
    public ResponseEntity<ApiResponseBody> inactive(@PathVariable("id") String id) throws BaseException {
        inventoryService.inactive(id);
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseBody> delete(@PathVariable("id") String id) throws BaseException {
        inventoryService.delete(Long.valueOf(id));
        ApiResponseBody body = new ApiResponseBody("success");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/dropdown/{concertId}")
    public ResponseEntity<ApiResponseBody> dropdown(@PathVariable("concertId") Long concertId) {
        List<DropdownOptions> dateDropdownOptions = inventoryService.findDropdownOptions(concertId);
        ApiResponseBody body = new ApiResponseBody(dateDropdownOptions);
        return ResponseEntity.ok(body);
    }
}
