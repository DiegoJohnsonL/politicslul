package com.example.jobagapi.cucumber;

import com.example.jobagapi.JobagApiApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@CucumberOptions(
        plugin = {"pretty", "json:target/cucumber.json"},
        features = "src/test/resources/features"
)
@SpringBootTest(classes = {
        JobagApiApplication.class,
        CucumberRunnerTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CucumberRunnerTest {
}
