package com.example.BloodDonationProject.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

public class PaginationUtils {

    public static class PaginationParams {
        private int skip;
        private int limit;
        private int page;
        private int perPage;

        public PaginationParams(int page, int perPage) {
            this.page = page;
            this.perPage = perPage;
            this.skip = (page - 1) * perPage;
            this.limit = perPage;
        }

        public int getSkip() {
            return skip;
        }

        public int getLimit() {
            return limit;
        }

        public int getPage() {
            return page;
        }

        public int getPerPage() {
            return perPage;
        }
    }

    public static PaginationParams calcPagination(int page, int perPage) {
        return new PaginationParams(page, perPage);
    }

    public static Pageable createPageable(int page, int perPage) {
        return PageRequest.of(page - 1, perPage);
    }

    public static <T> Map<String, Object> createPaginatedResponse(Page<T> page, String dataKey) {
        Map<String, Object> response = new HashMap<>();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", page.getNumber() + 1);
        pagination.put("perPage", page.getSize());
        pagination.put("total", page.getTotalElements());
        pagination.put("totalPages", page.getTotalPages());
        pagination.put("hasNext", page.hasNext());
        pagination.put("hasPrevious", page.hasPrevious());

        response.put("pagination", pagination);
        response.put(dataKey, page.getContent());

        return response;
    }

    public static <T> Map<String, Object> createPaginatedResponse(
            java.util.List<T> data,
            int page,
            int perPage,
            long total,
            String dataKey) {

        Map<String, Object> response = new HashMap<>();

        int totalPages = (int) Math.ceil((double) total / perPage);

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", page);
        pagination.put("perPage", perPage);
        pagination.put("total", total);
        pagination.put("totalPages", totalPages);
        pagination.put("hasNext", page < totalPages);
        pagination.put("hasPrevious", page > 1);

        response.put("pagination", pagination);
        response.put(dataKey, data);

        return response;
    }
}
