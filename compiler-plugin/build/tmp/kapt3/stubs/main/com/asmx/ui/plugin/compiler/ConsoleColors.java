package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\bA\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006E"}, d2 = {"Lcom/asmx/ui/plugin/compiler/ConsoleColors;", "", "()V", "BLACK", "", "BLACK_BACKGROUND", "BLACK_BACKGROUND_BRIGHT", "BLACK_BOLD", "BLACK_BOLD_BRIGHT", "BLACK_BRIGHT", "BLACK_UNDERLINED", "BLUE", "BLUE_BACKGROUND", "BLUE_BACKGROUND_BRIGHT", "BLUE_BOLD", "BLUE_BOLD_BRIGHT", "BLUE_BRIGHT", "BLUE_UNDERLINED", "BOLD", "CYAN", "CYAN_BACKGROUND", "CYAN_BACKGROUND_BRIGHT", "CYAN_BOLD", "CYAN_BOLD_BRIGHT", "CYAN_BRIGHT", "CYAN_UNDERLINED", "ENCIRCLED", "FRAMED", "GREEN", "GREEN_BACKGROUND", "GREEN_BACKGROUND_BRIGHT", "GREEN_BOLD", "GREEN_BOLD_BRIGHT", "GREEN_BRIGHT", "GREEN_UNDERLINED", "OVERLINED", "PURPLE", "PURPLE_BACKGROUND", "PURPLE_BACKGROUND_BRIGHT", "PURPLE_BOLD", "PURPLE_BOLD_BRIGHT", "PURPLE_BRIGHT", "PURPLE_UNDERLINED", "RED", "RED_BACKGROUND", "RED_BACKGROUND_BRIGHT", "RED_BOLD", "RED_BOLD_BRIGHT", "RED_BRIGHT", "RED_UNDERLINED", "RESET", "UNDERLINE", "WHITE", "WHITE_BACKGROUND", "WHITE_BACKGROUND_BRIGHT", "WHITE_BOLD", "WHITE_BOLD_BRIGHT", "WHITE_BOLD_BRIGHT_UNDERLINED", "WHITE_BOLD_UNDERLINED", "WHITE_BRIGHT", "WHITE_BRIGHT_UNDERLINED", "WHITE_UNDERLINED", "YELLOW", "YELLOW_BACKGROUND", "YELLOW_BACKGROUND_BRIGHT", "YELLOW_BOLD", "YELLOW_BOLD_BRIGHT", "YELLOW_BRIGHT", "YELLOW_UNDERLINED", "compiler-plugin"})
public final class ConsoleColors {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RESET = "\u001b[0m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BOLD = "\u001b[1m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String UNDERLINE = "\u001b[4m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String FRAMED = "\u001b[51m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ENCIRCLED = "\u001b[52m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String OVERLINED = "\u001b[53m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK = "\u001b[0;30m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED = "\u001b[0;31m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN = "\u001b[0;32m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW = "\u001b[0;33m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE = "\u001b[0;34m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE = "\u001b[0;35m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN = "\u001b[0;36m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE = "\u001b[0;37m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK_BOLD = "\u001b[1;30m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED_BOLD = "\u001b[1;31m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN_BOLD = "\u001b[1;32m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW_BOLD = "\u001b[1;33m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE_BOLD = "\u001b[1;34m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE_BOLD = "\u001b[1;35m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN_BOLD = "\u001b[1;36m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BOLD = "\u001b[1;37m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK_UNDERLINED = "\u001b[4;30m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED_UNDERLINED = "\u001b[4;31m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN_UNDERLINED = "\u001b[4;32m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW_UNDERLINED = "\u001b[4;33m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE_UNDERLINED = "\u001b[4;34m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE_UNDERLINED = "\u001b[4;35m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN_UNDERLINED = "\u001b[4;36m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_UNDERLINED = "\u001b[4;37m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BOLD_UNDERLINED = "\u001b[1;4;37m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK_BACKGROUND = "\u001b[40m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED_BACKGROUND = "\u001b[41m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN_BACKGROUND = "\u001b[42m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW_BACKGROUND = "\u001b[43m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE_BACKGROUND = "\u001b[44m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE_BACKGROUND = "\u001b[45m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN_BACKGROUND = "\u001b[46m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BACKGROUND = "\u001b[47m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK_BRIGHT = "\u001b[0;90m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED_BRIGHT = "\u001b[0;91m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN_BRIGHT = "\u001b[0;92m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW_BRIGHT = "\u001b[0;93m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE_BRIGHT = "\u001b[0;94m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE_BRIGHT = "\u001b[0;95m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN_BRIGHT = "\u001b[0;96m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BRIGHT = "\u001b[0;97m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BRIGHT_UNDERLINED = "\u001b[0;4;97m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK_BOLD_BRIGHT = "\u001b[1;90m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED_BOLD_BRIGHT = "\u001b[1;91m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN_BOLD_BRIGHT = "\u001b[1;92m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW_BOLD_BRIGHT = "\u001b[1;93m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE_BOLD_BRIGHT = "\u001b[1;94m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE_BOLD_BRIGHT = "\u001b[1;95m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN_BOLD_BRIGHT = "\u001b[1;96m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BOLD_BRIGHT = "\u001b[1;97m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BOLD_BRIGHT_UNDERLINED = "\u001b[1;4;97m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLACK_BACKGROUND_BRIGHT = "\u001b[0;100m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String RED_BACKGROUND_BRIGHT = "\u001b[0;101m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GREEN_BACKGROUND_BRIGHT = "\u001b[0;102m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String YELLOW_BACKGROUND_BRIGHT = "\u001b[0;103m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLUE_BACKGROUND_BRIGHT = "\u001b[0;104m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PURPLE_BACKGROUND_BRIGHT = "\u001b[0;105m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CYAN_BACKGROUND_BRIGHT = "\u001b[0;106m";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WHITE_BACKGROUND_BRIGHT = "\u001b[0;107m";
    public static final com.asmx.ui.plugin.compiler.ConsoleColors INSTANCE = null;
    
    private ConsoleColors() {
        super();
    }
}