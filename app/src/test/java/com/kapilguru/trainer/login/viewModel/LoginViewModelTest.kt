package com.kapilguru.trainer.login.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.kapilguru.trainer.ApiHelper
import com.kapilguru.trainer.MainCoroutineRule
import com.kapilguru.trainer.TestCoroutineRule
import com.kapilguru.trainer.getOrAwaitValue
import com.kapilguru.trainer.login.LoginRepository
import com.kapilguru.trainer.login.LoginRepositoryFake
import com.kapilguru.trainer.login.models.Data
import com.kapilguru.trainer.login.models.LoginResponse
import com.kapilguru.trainer.login.models.LoginUserRequest
import com.kapilguru.trainer.network.ApiResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.MockitoAnnotations.openMocks


@ExperimentalCoroutinesApi
open class LoginViewModelTest{

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val mainCoroutineRule= MainCoroutineRule()

    @Mock
    private lateinit var apiHelper: ApiHelper

    @Mock
    private lateinit var loginRepository: LoginRepository

//    @Mock
//     var  loginRepositoryFake: LoginRepositoryFake = mock(LoginRepositoryFake::class.java)
    private  var loginRepositoryFake: LoginRepositoryFake = Mockito.spy(LoginRepositoryFake::class.java)

//    @Mock
     lateinit var loginViewModel: LoginViewModel


    var userName: MutableLiveData<String> = MutableLiveData()

    var password: MutableLiveData<String> = MutableLiveData()

    @Before
    fun setUp() {
        openMocks(this)
//        loginRepository = LoginRepository(apiHelper)
//        loginRepositoryFake = LoginRepositoryFake()
        loginViewModel = LoginViewModel(loginRepositoryFake)
        loginViewModel = spy(loginViewModel)
    }

    @Test
    fun `correct credentials`() {
        userName.value = "abcs@g.com"
        password.value = "9642863698"
        val abc = loginViewModel.isInputValid(userName.value,password.value)
        assertThat(abc).isTrue()
    }

    @Test
    fun `in correct user name return false`() {
        userName.value = "ab@g"
        password.value = "9642863698"
        val abc = loginViewModel.isInputValid(userName.value,password.value)
        assertThat(abc).isFalse()
    }

    @Test
    fun `in correct password  return false`() {
        userName.value = "abc@g.com"
        password.value = "964"
        val abc = loginViewModel.isInputValid(userName.value,password.value)
        assertThat(abc).isFalse()
    }

    @Test
    fun `correct api call`() {
        userName.value = "abc@g.com"
        password.value = "964"
        loginViewModel.loginUserApi(validMailPassReturnsTrue())
        verify((loginViewModel), times(1)).onClickCheck()
        testCoroutineRule.runBlockingTest {
            doReturn(successResponse).`when`(loginViewModel).getresultDat()
//            `when`(loginViewModel.getresultDat().getOrAwaitValue()).thenReturn(ApiResource.success(successResponse()))
            val abc = loginViewModel.getresultDat()
//            verify(loginViewModel, times(1)).onClickCheck()
            assertThat(abc).isEqualTo(successResponse)

        }
    }

    val successResponse = MutableLiveData(ApiResource.success(LoginResponse(
        Data(
            auth = true,
            contactNumber = "9989",
            email = "abc@gmail.com",
            isAdmin = 1,
            isTrainer = 1,
            isStudent = 1,
            token = "123",
            user_id = 1,
            username = "123",
            isProfileUpdated = 1,
            isBankUpdated = 1,
            isImageUpdated = 1,
            user_code = "1",
            isSubscribed = 1
        ), "succes", 200))
    )


    private fun validMailPassReturnsTrue() = LoginUserRequest("abc@gmail.com","123")




    fun getNonNullNonEmptyUserNameAndPassword() {
        loginViewModel.userName.value = "ABC"
        loginViewModel.password.value = "ABC"
    }
}