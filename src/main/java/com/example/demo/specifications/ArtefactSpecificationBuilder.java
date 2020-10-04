package com.example.demo.specifications;

import com.example.demo.models.Artefact;
import com.example.demo.util.SearchOperation;
import com.example.demo.util.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArtefactSpecificationBuilder {
    //    private final List<SearchCriteria> params;
//
//    public ArtefactSpecificationBuilder() {
//        params = new ArrayList<SearchCriteria>();
//    }
//
//    public ArtefactSpecificationBuilder with(String key, String operation, Object value) {
//        params.add(new SearchCriteria(key, operation, value));
//        return this;
//    }
//
//    public Specification<Artefact> build() {
//        if (params.size() == 0) {
//            return null;
//        }
//
//        List<Specification> specs = params.stream()
//                .map(ArtefactSpecification::new)
//                .collect(Collectors.toList());
//
//        Specification result = specs.get(0);
//
//        for (int i = 1; i < params.size(); i++) {
//            result = params.get(i)
//                    .isOrPredicate()
//                    ? Specification.where(result)
//                    .or(specs.get(i))
//                    : Specification.where(result)
//                    .and(specs.get(i));
//        }
//        return result;
//    }
    private final List<SpecSearchCriteria> params;

    public ArtefactSpecificationBuilder() {
        params = new ArrayList<>();
    }

    // API

    public final ArtefactSpecificationBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final ArtefactSpecificationBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<Artefact> build() {
        if (params.size() == 0)
            return null;

        Specification<Artefact> result = new ArtefactSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new ArtefactSpecification(params.get(i)))
                    : Specification.where(result).and(new ArtefactSpecification(params.get(i)));
        }

        return result;
    }

    public final ArtefactSpecificationBuilder with(ArtefactSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final ArtefactSpecificationBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
