package com.backend.service.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagedResponseDto<T> {

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public static <T> PagedResponseDto<T> from(Page<T> page) {
        return new PagedResponseDto<T>()
                .setItems(page.getContent())
                .setPage(page.getNumber())
                .setSize(page.getSize())
                .setTotalElements(page.getTotalElements())
                .setTotalPages(page.getTotalPages())
                .setHasNext(page.hasNext())
                .setHasPrevious(page.hasPrevious());
    }

    public List<T> getItems() {
        return items;
    }

    public PagedResponseDto<T> setItems(List<T> items) {
        this.items = items;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PagedResponseDto<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public PagedResponseDto<T> setSize(int size) {
        this.size = size;
        return this;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public PagedResponseDto<T> setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public PagedResponseDto<T> setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public PagedResponseDto<T> setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public PagedResponseDto<T> setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        return this;
    }
}

