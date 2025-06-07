package com.melvstein.solar_system.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Component
public class BaseSpecification<T> {
    public Specification<T> getSpecificationByFilter(List<String> filter) {
        Specification<T> spec = Specification.where(null);
        Specification<T> nextSpec = null;

        if (filter != null) {
            for (String param : filter) {
                String[] parts = param.split(":");

                if (parts.length == 1) {
                    log.warn("Skipped filter param={}", param);
                    continue;
                };

                String logic = parts[0];
                String field = parts[1];

                if (parts.length == 3) {
                    String value = parts[2];
                    String[] nestedFields = field.split("\\.");

                    // join table
                    if (nestedFields.length == 2 && !nestedFields[0].isBlank() && !nestedFields[1].isBlank()) {
                        String table = nestedFields[0];
                        field = nestedFields[1];

                        nextSpec = buildSpecificationJoin(table, field, value);
                    } else {
                        nextSpec = buildSpecification(field, value);
                    }
                } else if (parts.length == 4) {
                    String operator = parts[2];
                    String value = parts[3];

                    String[] nestedFields = field.split("\\.");

                    // join table
                    if (nestedFields.length == 2 && !nestedFields[0].isBlank() && !nestedFields[1].isBlank()) {
                        String table = nestedFields[0];
                        field = nestedFields[1];

                        nextSpec = buildSpecificationJoin(table, field, operator, value);
                    } else {
                        nextSpec = buildSpecification(field, operator, value);
                    }
                }

                if (nextSpec != null) {
                    spec = switch (logic) {
                        case "and" -> spec.and(nextSpec);
                        case "or" -> spec.or(nextSpec);
                        default -> throw new IllegalArgumentException("Invalid logic operator: " + logic);
                    };
                }
            }
        }

        return spec;
    }

    public Specification<T> buildSpecification(String field, String value) {
        return (root, query, builder) -> {
            return builder.equal(root.get(field), value);
        };
    }

    public Specification<T> buildSpecification(String field, String operator, String value) {
        return (root, query, builder) -> {
            return switch (operator) {
                case "eq" -> builder.equal(root.get(field), value);
                case "ne" -> builder.notEqual(root.get(field), value);
                case "gt" -> builder.greaterThan(root.get(field), value);
                case "lt" -> builder.lessThan(root.get(field), value);
                case "gte" -> builder.greaterThanOrEqualTo(root.get(field), value);
                case "lte" -> builder.lessThanOrEqualTo(root.get(field), value);
                case "like" -> builder.like(root.get(field), "%" + value + "%");
                case "in" -> builder.in(root.get(field)).value(List.of(value.split(",")));
                default  -> throw new IllegalArgumentException("Invalid operator: " + operator);
            };
        };
    }

    public Specification<T> buildSpecificationJoin(String table, String field, String value) {
        return (root, query, builder) -> {
            try {
                root.getModel().getAttribute(table);
                Join<T, ?> moonJoin = root.join(table, JoinType.LEFT);

                return builder.equal(moonJoin.get(field), value);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid join attribute: " + table);
            }
        };
    }

    public Specification<T> buildSpecificationJoin(String table, String field, String operator, String value) {
        return (root, query, builder) -> {
            try {
                root.getModel().getAttribute(table);
                Join<T, ?> moonJoin = root.join(table, JoinType.LEFT);

                return switch (operator) {
                    case "eq" -> builder.equal(moonJoin.get(field), value);
                    case "ne" -> builder.notEqual(moonJoin.get(field), value);
                    case "gt" -> builder.greaterThan(moonJoin.get(field), value);
                    case "lt" -> builder.lessThan(moonJoin.get(field), value);
                    case "gte" -> builder.greaterThanOrEqualTo(root.get(field), value);
                    case "lte" -> builder.lessThanOrEqualTo(root.get(field), value);
                    case "like" -> builder.like(root.get(field), "%" + value + "%");
                    case "in" -> builder.in(root.get(field)).value(List.of(value.split(",")));
                    default  -> throw new IllegalArgumentException("Invalid operator: " + operator);
                };
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid join attribute: " + table);
            }
        };
    }
}
