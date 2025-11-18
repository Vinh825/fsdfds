import { createContext, useContext, useEffect, useMemo, useState } from "react";

const ThemeContext = createContext({
    theme: "dark",
    toggleTheme: () => { },
});

const THEME_STORAGE_KEY = "hl-theme";
const BODY_THEME_CLASSES = ["theme-dark", "theme-light"];

const getPreferredTheme = () => {
    if (typeof window === "undefined") return "dark";
    const stored = localStorage.getItem(THEME_STORAGE_KEY);
    if (stored === "dark" || stored === "light") {
        return stored;
    }
    const prefersLight = window.matchMedia && window.matchMedia("(prefers-color-scheme: light)").matches;
    return prefersLight ? "light" : "dark";
};

export function ThemeProvider({ children }) {
    const [theme, setTheme] = useState(getPreferredTheme);

    useEffect(() => {
        if (typeof document === "undefined") return;
        document.body.classList.remove(...BODY_THEME_CLASSES);
        const nextClass = `theme-${theme}`;
        document.body.classList.add(nextClass);
        localStorage.setItem(THEME_STORAGE_KEY, theme);
    }, [theme]);

    const toggleTheme = () => {
        setTheme(prev => (prev === "dark" ? "light" : "dark"));
    };

    const value = useMemo(() => ({ theme, toggleTheme }), [theme]);

    return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
}

export function useTheme() {
    return useContext(ThemeContext);
}

