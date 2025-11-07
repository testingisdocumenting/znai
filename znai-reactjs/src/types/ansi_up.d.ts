// ansi_up.d.ts

declare module 'ansi_up' {
    /**
     * Options for configuring AnsiUp behavior
     */
    export interface AnsiUpOptions {
        /**
         * Enable parsing of 256-color ANSI codes
         * @default true
         */
        use_classes?: boolean;

        /**
         * Escape HTML characters in the input text
         * @default false
         */
        escape_for_html?: boolean;
    }

    /**
     * Packet containing text and foreground/background colors
     */
    export interface TextPacket {
        text: string;
        fg?: string;
        bg?: string;
        bold?: boolean;
        italic?: boolean;
        underline?: boolean;
        strikethrough?: boolean;
    }

    /**
     * AnsiUp - Convert ANSI escape codes to HTML or text
     */
    export class AnsiUp {
        constructor();

        /**
         * Convert ANSI escape codes to HTML
         * @param text - Input text containing ANSI codes
         * @returns HTML string with ANSI codes converted to span elements
         */
        ansi_to_html(text: string): string;

        /**
         * Convert ANSI escape codes to text (strips ANSI codes)
         * @param text - Input text containing ANSI codes
         * @returns Plain text with ANSI codes removed
         */
        ansi_to_text(text: string): string;

        /**
         * Convert ANSI escape codes to an array of text packets
         * @param text - Input text containing ANSI codes
         * @returns Array of text packets with styling information
         */
        ansi_to_json(text: string): TextPacket[];

        /**
         * Use classes instead of inline styles for colors
         */
        use_classes: boolean;

        /**
         * Escape HTML in the input text
         */
        escape_for_html: boolean;

        /**
         * Set the palette for 256-color mode
         * @param palette - Array of 256 color strings (hex format)
         */
        setupPalette(palette?: string[]): void;

        /**
         * URL pattern for linkifying URLs in the text
         */
        url_whitelist: { [scheme: string]: boolean };
    }
}