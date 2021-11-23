namespace utils {
namespace second {

/**
 * **Domain** specific context setting. Describes business setting and requirements.
 */
class AnotherClass {
public:
    /// current number of `sounds` made
    int number_of_sounds;

    static int counter;

    ///
    /// sing out loud
    /// @param tone tone at which to sing
    /// @param volume volume at which to sing
    ///
    void sing(int tone, int volume) const;

    ///
    /// bark out loud
    /// @param myClass to test reference
    /// @param tone tone at which to sing
    /// @param volume volume at which to sing
    ///
    void bark(const MyClass& myClass, int tone, int volume);

    ///
    /// global smile
    /// @param volume smile volume
    ///
    static void smile(int volume) {
    }

protected:
    ///
    /// ask for help out loud
    /// *example*
    /// ```cpp
    /// utils::second::AnotherClass* instance;
    /// instance->help(10, 20);
    /// ```
    /// @param tone tone at which to sing
    /// @param volume volume at which to sing
    ///
    virtual void help(int tone, int volume);

};

}
}