package com.backend.controller;

import com.backend.service.CalendarService;
import com.backend.service.dto.AssignRecipeToSlotRequestDto;
import com.backend.service.dto.CalendarDayResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/calendar")
@Tag(name = "Calendar", description = "Daily meal slots and recipe assignment")
public class CalendarController implements SecuredRestController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/day")
    @Operation(summary = "Day view", description = "Slots 0..mealsPerDay-1 for the given date; each may already have a recipe.")
    public ResponseEntity<CalendarDayResponseDto> getDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(calendarService.getDay(date));
    }

    @PutMapping("/day/slot")
    @Operation(summary = "Assign recipe to a meal slot", description = "Recipe must match your dietary restrictions (same rules as /recipes/recommended).")
    public ResponseEntity<CalendarDayResponseDto> assignRecipe(@RequestBody AssignRecipeToSlotRequestDto body) {
        return ResponseEntity.ok(calendarService.assignRecipe(body));
    }
}
