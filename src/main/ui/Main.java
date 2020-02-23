package ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import model.*;

import java.io.*;
import java.time.Clock;

public class Main {
    // TODO: determine how to get Jackson Modules working without Maven. No .jar appears to be available. Necessary for
    //       deserialization of LocalDateTime fields.
    public static void main(String[] args) throws IOException {
        new GitBreadApp();
    }
}
