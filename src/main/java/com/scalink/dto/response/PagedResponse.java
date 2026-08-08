package com.scalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    public List<T> getContent() { return this.content; }
    public int getPage() { return this.page; }
    public int getSize() { return this.size; }
    public long getTotalElements() { return this.totalElements; }
    public int getTotalPages() { return this.totalPages; }
    public boolean getHasNext() { return this.hasNext; }
    public void setContent(List<T> content) { this.content = content; }
    public void setPage(int page) { this.page = page; }
    public void setSize(int size) { this.size = size; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
    public PagedResponse() {}
    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean hasNext) { this.content = content; this.page = page; this.size = size; this.totalElements = totalElements; this.totalPages = totalPages; this.hasNext = hasNext; }
    public static <T> PagedResponseBuilder<T> builder() { return new PagedResponseBuilder<T>(); }
    public static class PagedResponseBuilder<T> {
        private List<T> content;
        public PagedResponseBuilder content(List<T> content) { this.content = content; return this; }
        private int page;
        public PagedResponseBuilder page(int page) { this.page = page; return this; }
        private int size;
        public PagedResponseBuilder size(int size) { this.size = size; return this; }
        private long totalElements;
        public PagedResponseBuilder totalElements(long totalElements) { this.totalElements = totalElements; return this; }
        private int totalPages;
        public PagedResponseBuilder totalPages(int totalPages) { this.totalPages = totalPages; return this; }
        private boolean hasNext;
        public PagedResponseBuilder hasNext(boolean hasNext) { this.hasNext = hasNext; return this; }
        public PagedResponse build() { return new PagedResponse(content, page, size, totalElements, totalPages, hasNext); }
    }
}
