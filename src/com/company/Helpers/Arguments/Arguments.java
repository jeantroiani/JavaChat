package com.company.Helpers.Arguments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arguments {

    public static Map<String, Object> getArgumentsMap(String[] args, List<String> listOfValidArguments) {
        int idx = 0;
        int argsSize = args.length;

        Map<String, Object> map = new HashMap<>();

        while (idx <= argsSize - 2) {
            if (listOfValidArguments.contains(args[idx])) {
                String key = args[idx];
                Object value = args[idx + 1];
                map.put(key, value);
            }
            idx += 2;
        }
        return map;
    }
}
