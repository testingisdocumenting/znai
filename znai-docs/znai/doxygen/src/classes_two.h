namespace utils {
namespace second {

/**
 * **Domain** specific context setting. Describes business setting and requirements.
 */
class AnotherClass {
public:
    /// current number of sounds made
    int number_of_sounds;

    static int counter;

    ///
    /// sing out loud
    /// @param tone tone at which to sing
    /// @param volume volume at which to sing
    ///
    void sing(int tone, int volume);

    ///
    /// bark out loud
    /// @param tone tone at which to sing
    /// @param volume volume at which to sing
    ///
    void bark(int tone, int volume);


protected:
    ///
    /// ask for help out loud
    /// @param tone tone at which to sing
    /// @param volume volume at which to sing
    ///
    virtual void help(int tone, int volume);

};

}
}