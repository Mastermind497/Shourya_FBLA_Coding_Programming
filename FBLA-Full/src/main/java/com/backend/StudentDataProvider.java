package com.backend;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StudentDataProvider extends AbstractBackEndDataProvider<StudentData, CrudFilter> {
    final ArrayList<StudentData> studentData = FileMethods.getStudentData();
    private Consumer<Long> sizeChangeListener;

    public static Predicate<StudentData> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<StudentData>) studentData -> {
                    try {
                        Object value = valueOf(constraint.getKey(), studentData);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .reduce(Predicate::and)
                .orElse(e -> true);
    }

    public static Comparator<StudentData> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<StudentData> comparator
                                = Comparator.comparing(studentData ->
                                (Comparable) valueOf(sortClause.getKey(), studentData));

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;
                    } catch (Exception ex) {
                        return (Comparator<StudentData>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    public static Object valueOf(String fieldName, StudentData studentData) {
        try {
            Field field = StudentData.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(studentData);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Stream<StudentData> fetchFromBackEnd(Query<StudentData, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<StudentData> stream = studentData.stream();

        if (query.getFilter().isPresent()) {
            stream = stream
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return null;
    }

    @Override
    protected int sizeInBackEnd(Query<StudentData, CrudFilter> query) {
        return 0;
    }

    public void persist(StudentData item) {
        final Optional<StudentData> existingItem = find(item.getStudentID());
        if (existingItem.isPresent()) {
            int position = studentData.indexOf(existingItem.get());
            studentData.remove(existingItem.get());
            studentData.add(position, item);
        } else {
            studentData.add(item);
        }
    }

    Optional<StudentData> find(Integer id) {
        return studentData
                .stream()
                .filter(entity -> entity.getStudentID() == id)
                .findFirst();
    }

    public void delete(StudentData item) {
        studentData.removeIf(entity -> entity.getStudentID() == (item.getStudentID()));
    }
}
