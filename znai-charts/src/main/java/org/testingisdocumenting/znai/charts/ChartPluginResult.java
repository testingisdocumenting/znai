/*
 * Copyright 2022 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.charts;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ChartPluginResult {
    private ChartPluginResult() {
    }

    static PluginResult create(PluginParams pluginParams, String type, String csvContent) {
        ChartData chartData = ChartDataCsvParser.parse(csvContent);

        List<Object> columns = pluginParams.getOpts().getList(ChartIncludeBasePlugin.COLUMNS);
        if (!columns.isEmpty()) {
            // first we need to create indexes of the columns to be removed
            // sort decending, so we can call remove without fear
            List<Integer> indexesToDelete = chartData.getLabels().stream()
                    .filter(col -> !columns.contains(col))
                    .map(col -> chartData.getLabels().indexOf(col))
                    .sorted(Comparator.reverseOrder())
                    .toList();


            if (!indexesToDelete.isEmpty()) {
                // remove labels
                for (int idx : indexesToDelete) {
                    chartData.getLabels().remove(idx);
                }

                // remove data
                for (List<Object> row : chartData.getData()) {
                    for (int idx : indexesToDelete) {
                        row.remove(idx);
                    }
                }
            }
        }

        List<List<Object>> data = chartData.getData();
        List<Object> breakpoints = pluginParams.getOpts().getList(ChartIncludeBasePlugin.BREAKPOINT_KEY);
        boolean isTimeSeries = pluginParams.getOpts().get(ChartIncludeBasePlugin.TIME_KEY, false);

        validateBreakpoints(breakpoints, data);

        List<Object> convertedBreakpoints = convertAllBreakpointsToActualValues(breakpoints, data);

        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("chartType", type);
        props.put("isTimeSeries", isTimeSeries);

        if (!breakpoints.isEmpty()) {
            props.put("breakpoint", convertedBreakpoints);
        }

        props.putAll(chartData.toMap());

        return PluginResult.docElement("EchartGeneric", props);
    }

    private static List<Object> convertAllBreakpointsToActualValues(List<Object> breakpoints, List<List<Object>> data) {
        if (!isAllBreakpoints(breakpoints)) {
            return breakpoints;
        }

        if (data.stream().anyMatch(row -> row.get(0) instanceof Number)) {
            throw new IllegalArgumentException("<all> breakpoint is not supported for numerical data");
        }

        return data.stream()
                .limit(data.size() - 1)
                .map(row -> row.get(0)).collect(Collectors.toList());
    }

    private static void validateBreakpoints(List<Object> breakpoints, List<List<Object>> data) {
        if (breakpoints.isEmpty() || isAllBreakpoints(breakpoints)) {
            return;
        }

        if (data.isEmpty()) {
            return;
        }

        Stream<Object> mainAxisValues = data.stream().map(row -> row.get(0));

        if (breakpoints.get(0) instanceof String) {
            validateTextBreakPoints(breakpoints, mainAxisValues);
        } else {
            validateNumericBreakpoints(breakpoints, mainAxisValues);
        }
    }

    private static void validateTextBreakPoints(List<Object> breakpoints, Stream<Object> mainAxisValues) {
        Set<String> uniqueValues = mainAxisValues
                .map(Object::toString)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Object breakpoint : breakpoints) {
            if (!uniqueValues.contains(breakpoint.toString())) {
                throw new IllegalArgumentException("breakpoint value <" + breakpoint + "> is not found, " +
                        "available values:\n  " + String.join("\n  ", uniqueValues));
            }
        }
    }

    private static void validateNumericBreakpoints(List<Object> breakpoints, Stream<Object> mainAxisValues) {
        DoubleSummaryStatistics statistics = mainAxisValues.collect(
                Collectors.summarizingDouble(v -> ((Number) v).doubleValue()));

        for (Object breakpoint : breakpoints) {
            double breakpointNumber = ((Number) breakpoint).doubleValue();
            if (breakpointNumber < statistics.getMin() || breakpointNumber > statistics.getMax()) {
                throw new IllegalArgumentException("breakpoint <" + breakpointNumber + "> is outside of range" +
                        " [" + statistics.getMin() + ", " + statistics.getMax() + "]");
            }
        }
    }

    private static boolean isAllBreakpoints(List<Object> breakpoints) {
        return breakpoints.size() == 1 && breakpoints.get(0).equals("all");
    }
}
