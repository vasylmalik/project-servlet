package com.tictactoe;

import java.util.*;
import java.util.stream.Collectors;

public class Field {
    private final Map<Integer, Sign> field;

    public Field() {
        field = new HashMap<>();
        field.put(0, Sign.EMPTY);
        field.put(1, Sign.EMPTY);
        field.put(2, Sign.EMPTY);
        field.put(3, Sign.EMPTY);
        field.put(4, Sign.EMPTY);
        field.put(5, Sign.EMPTY);
        field.put(6, Sign.EMPTY);
        field.put(7, Sign.EMPTY);
        field.put(8, Sign.EMPTY);
    }

    public Field(Map<Integer, Sign> anotherField){
        field = new HashMap<>(anotherField);
    }

    public Map<Integer, Sign> getField() {
        return field;
    }

    public int getFieldIndex() {
        Field imagineField = new Field(field);

        int index = getWinningIndex(Sign.NOUGHT, imagineField); //find winning move
        if(index >= 0) return index;

        index = getWinningIndex(Sign.CROSS, imagineField); //predict cross winning
        if(index >= 0) return index;

        return field.entrySet().stream()
                .filter(e -> e.getValue() == Sign.EMPTY)
                .map(Map.Entry::getKey)
                .findFirst().orElse(-1);
    }

    public List<Sign> getFieldData() {
        return field.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public boolean checkWin(Sign sign) {
        List<List<Integer>> winPossibilities = List.of(
                List.of(0, 1, 2),
                List.of(3, 4, 5),
                List.of(6, 7, 8),
                List.of(0, 3, 6),
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(0, 4, 8),
                List.of(2, 4, 6)
        );

        for (List<Integer> winPossibility : winPossibilities) {
            if (field.get(winPossibility.get(0)) == sign
                && field.get(winPossibility.get(1)) == sign
                && field.get(winPossibility.get(2)) == sign) {
                return true;
            }
        }
        return false;
    }
    public int getWinningIndex(Sign sign, Field imagineField) {
        for (Map.Entry<Integer, Sign> entry: field.entrySet()) {
            if(entry.getValue() == Sign.EMPTY){
                imagineField.getField().put(entry.getKey(), sign);
                if(imagineField.checkWin(sign)){
                    return entry.getKey();
                }
                imagineField.getField().put(entry.getKey(), Sign.EMPTY);
            }
        }
        return -1;
    }


}