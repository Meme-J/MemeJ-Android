package com.example.memej

import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.memej.ui.auth.SignUpActivity
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    private val EMPTY_USERNAME_ERROR: String = "Username cannot be empty"
    private val EMPTY_PASSWORD_ERROR: String = "Password cannot be empty"
    private val EMPTY_EMAIL_ERROR: String = "Email cannot be empty"
    private val EMPTY_NAME_ERROR: String = "Name cannot be empty"

    /**
     * This basically setups the SignUpActivity before test
     */

    @get:Rule
    var mActivityRule: ActivityTestRule<SignUpActivity> =
        ActivityTestRule(SignUpActivity::class.java)

    /**
     * This method is used to find the EditText within the TextInputLayout. Useful for typing into the TextInputLayout
     */

    fun findEditTextInTextInputLayout(@IdRes textInputLayoutId: Int): ViewInteraction {

        return Espresso.onView(
            Matchers.allOf(
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(textInputLayoutId)),
                ViewMatchers.isAssignableFrom(EditText::class.java)
            )
        )
    }


    companion object {

        /**
         * This method simply implements the null check, checks the types and casts
         */
        fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                /**
                 * Generates a description of the object.  The description may be part of a
                 * a description of a larger object of which this is just a component, so it
                 * should be worded appropriately.
                 *
                 * @param description
                 * The description to be built or appended to.
                 */
                override fun describeTo(description: Description?) {
                    //("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                /**
                 * Subclasses should implement this. The item will already have been checked for
                 * the specific type and will never be null.
                 */
                override fun matchesSafely(item: View?): Boolean {

                    if ((item !is TextInputLayout)) {
                        return false
                    }


                    val error: CharSequence? = item.error
                    if (error == null) {
                        return false
                    }

                    var errorMsg: String = error.toString()
                    return expectedErrorText.equals(errorMsg)

                }

            }

        }

    }

    /**
     * This test method is defined to test that when all the fields are empty and the user
     * clicks the signUp button then the errors in all the fields are displayed or not
     */
    @Test
    fun testSignUpClickedWhenAllFieldsAreEmpty() {

        findEditTextInTextInputLayout(R.id.tilName).perform(
            ViewActions.typeText(""),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilUsername).perform(
            ViewActions.typeText(""),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilEmail).perform(
            ViewActions.typeText(""),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilPassword).perform(
            ViewActions.typeText(""),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilConfPassword).perform(
            ViewActions.typeText(""),
            ViewActions.closeSoftKeyboard()
        )


        Espresso.onView(withId(R.id.tilName))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText(EMPTY_NAME_ERROR)))
        Espresso.onView(withId(R.id.tilUsername))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR)))
        Espresso.onView(withId(R.id.tilEmail))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText(EMPTY_EMAIL_ERROR)))
        Espresso.onView(withId(R.id.tilPassword))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR)))

    }

    /**
     * This test method simply checks that if the password and confirm password are unequal
     * and user clicks on signUp button then the error message is coming in confirm
     * password editText
     */
    @Test
    fun whenPassAreUnequalShowsErrorTest() {
        findEditTextInTextInputLayout(R.id.tilName).perform(
            ViewActions.typeText("name"),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilUsername).perform(
            ViewActions.typeText("username"),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilEmail).perform(
            ViewActions.typeText("email@test.com"),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilPassword).perform(
            ViewActions.typeText("qwertz123!"),
            ViewActions.closeSoftKeyboard()
        )
        findEditTextInTextInputLayout(R.id.tilConfPassword).perform(
            ViewActions.typeText("qwertz123!45"),
            ViewActions.closeSoftKeyboard()
        )


        //Error on this
        Espresso.onView(withId(R.id.tilConfPassword))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText("Passwords didn't match!")))
    }

    /**
     * This test method simply checks that if the password is too weak
     * and user clicks on signUp button then the error message is coming in
     * password editText
     */

}
