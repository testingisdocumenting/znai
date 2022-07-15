package my.company;

enum MyEnum {
    /**
     * description of <b>entry one</b>
     */
    ENTRY_ONE,

    /**
     * description of entry two
     * <ul>
     *     <li>item one</li>
     *     <li>item two</li>
     * </ul>
     */
    ENTRY_TWO,

    /**
     * Don't use, instead use ENTRY_TWO
     */
    @Deprecated
    ENTRY_THREE
}