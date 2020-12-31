package com.n11.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.n11.scheduler.entities.Seminar;
import com.n11.scheduler.repositories.SeminarRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.n11.scheduler.controllers.SeminarController;

public class SeminarControllerUnitTest {

    private static SeminarController seminarController;
    private static SeminarRepository mockedSeminarRepository;
    private static BindingResult mockedBindingResult;
    private static Model mockedModel;

    @BeforeClass
    public static void setUpSeminarControllerInstance() {
        mockedSeminarRepository = mock(SeminarRepository.class);
        mockedBindingResult = mock(BindingResult.class);
        mockedModel = mock(Model.class);
        seminarController = new SeminarController(mockedSeminarRepository);
    }

    @Test
    public void whenCalledIndex_thenCorrect() {
        assertThat(seminarController.showSeminarList(mockedModel)).isEqualTo("index");
    }

    @Test
    public void whenCalledshowSignUpForm_thenCorrect() {
        Seminar seminar = new Seminar("Overdoing it in Python", 45);

        assertThat(seminarController.showSignUpForm(seminar)).isEqualTo("add-seminar");
    }
    
    @Test
    public void whenCalledaddSeminarAndValidSeminar_thenCorrect() {
        Seminar seminar = new Seminar("Overdoing it in Python", 45);

        when(mockedBindingResult.hasErrors()).thenReturn(false);

        assertThat(seminarController.addSeminar(seminar, mockedBindingResult, mockedModel)).isEqualTo("redirect:/index");
    }

    @Test
    public void whenCalledaddSeminarAndInValidSeminar_thenCorrect() {
        Seminar seminar = new Seminar("Overdoing it in Python", 45);

        when(mockedBindingResult.hasErrors()).thenReturn(true);

        assertThat(seminarController.addSeminar(seminar, mockedBindingResult, mockedModel)).isEqualTo("add-seminar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCalledshowUpdateForm_thenIllegalArgumentException() {
        assertThat(seminarController.showUpdateForm(0, mockedModel)).isEqualTo("update-seminar");
    }
    
    @Test
    public void whenCalledupdateSeminarAndValidSeminar_thenCorrect() {
        Seminar seminar = new Seminar("Overdoing it in Python", 45);

        when(mockedBindingResult.hasErrors()).thenReturn(false);

        assertThat(seminarController.updateSeminar(1l, seminar, mockedBindingResult, mockedModel)).isEqualTo("redirect:/index");
    }

    @Test
    public void whenCalledupdateSeminarAndInValidSeminar_thenCorrect() {
        Seminar seminar = new Seminar("Overdoing it in Python", 45);

        when(mockedBindingResult.hasErrors()).thenReturn(true);

        assertThat(seminarController.updateSeminar(1l, seminar, mockedBindingResult, mockedModel)).isEqualTo("update-seminar");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void whenCalleddeleteSeminar_thenIllegalArgumentException() {
        assertThat(seminarController.deleteSeminar(1l, mockedModel)).isEqualTo("redirect:/index");
    }
}
