package com.park.monitoring.controller;

import com.park.monitoring.dto.BarChartData;
import com.park.monitoring.dto.DoughnutChartData;
import com.park.monitoring.dto.LineChartData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Controller
public class ChartController {

    @GetMapping("/monitor")
    public String insight(){
        return "insight";
    }
    @GetMapping("/chart")
    public String chart() {
        return "chart";
    }
}

@RestController
class AcquisitionController {

    @GetMapping("/api/acquisitions")
    public List<LineChartData> getAcquisitions() {
        return List.of(
                new LineChartData("2022.01", 12),
                new LineChartData("2022.02", 27),
                new LineChartData("2022.03", 56),
                new LineChartData("2022.04", 28),
                new LineChartData("2022.05", 60),
                new LineChartData("2022.06", 123),
                new LineChartData("2022.07", 23),
                new LineChartData("2022.08", 96),
                new LineChartData("2022.09", 58),
                new LineChartData("2022.10", 220),
                new LineChartData("2022.11", 82),
                new LineChartData("2022.12", 144),
                new LineChartData("2023.01", 12),
                new LineChartData("2023.02", 24),
                new LineChartData("2023.03", 36),
                new LineChartData("2023.04", 28),
                new LineChartData("2023.05", 60),
                new LineChartData("2023.06", 172),
                new LineChartData("2023.07", 84),
                new LineChartData("2023.08", 96),
                new LineChartData("2023.09", 58),
                new LineChartData("2023.10", 120),
                new LineChartData("2023.11", 82),
                new LineChartData("2023.12", 144)
        );
    }

    @GetMapping("/api/bar")
    public BarChartData getBarChartData() {
        List<String> labels = Arrays.asList("January", "February", "March", "April", "May", "June", "July");
        List<Integer> data = Arrays.asList(65, 59, 80, 81, 56, 55, 40);
        List<String> backgroundColor = Arrays.asList(
                "rgba(255, 99, 132, 0.2)",
                "rgba(255, 159, 64, 0.2)",
                "rgba(255, 205, 86, 0.2)",
                "rgba(75, 192, 192, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(153, 102, 255, 0.2)",
                "rgba(201, 203, 207, 0.2)"
        );
        List<String> borderColor = Arrays.asList(
                "rgb(255, 99, 132)",
                "rgb(255, 159, 64)",
                "rgb(255, 205, 86)",
                "rgb(75, 192, 192)",
                "rgb(54, 162, 235)",
                "rgb(153, 102, 255)",
                "rgb(201, 203, 207)"
        );
        return new BarChartData(labels, data, backgroundColor, borderColor);
    }

    @GetMapping("/api/doughnut/{id}")
    public DoughnutChartData getDoughnutChartData(@PathVariable String id) {
        List<String> labels = Arrays.asList("사용중","사용 가능");
        List<Integer> data = Arrays.asList(65,35);
        List<String> backgroundColor = Arrays.asList(
                "rgb(255, 99, 132)",
                "rgba(255, 159, 64)"        );
        return new DoughnutChartData(labels, data, backgroundColor);
    }

}
