package com.n11.scheduler.controllers;

import javax.validation.Valid;
import com.n11.scheduler.Event;
import com.n11.scheduler.entities.Seminar;
import com.n11.scheduler.repositories.SeminarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SeminarController {
    
    private final SeminarRepository seminarRepository;
    private List<String> errors = new ArrayList();
    private boolean showErrors=false;

    @Autowired
    public SeminarController(SeminarRepository seminarRepository) {
        this.seminarRepository = seminarRepository;
    }
    
    @GetMapping("/index")
    public String showSeminarList(Model model) {
        model.addAttribute("seminars", seminarRepository.findAll());

        model.addAttribute("errors", new ArrayList());
        model.addAttribute("showErrors", false);
        return "index";
    }
    
    @GetMapping("/form")
    public String showSignUpForm(Seminar seminar) {
        return "add-seminar";
    }
    
    @PostMapping("/addseminar")
    public String addSeminar(@Valid Seminar seminar, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-seminar";
        }
        
        seminarRepository.save(seminar);
        return "redirect:/index";
    }

    @GetMapping("/createAgenda")
    public String createAgenda(Model model) {

        long timeInMs = 7*3600*1000;//TODO: timeZone
        List<Event> events = new ArrayList<>();
        List<Seminar> seminars = (List<Seminar>) seminarRepository.findAll();
        List<Seminar> remainingSeminars = (List<Seminar>) seminarRepository.findAll();
        List<Event> morningEvents = getMorningEvents(remainingSeminars);
        if(morningEvents.size() > 0){
            List<Event> afternoonEvents =getAfternoonEvents(remainingSeminars);
            morningEvents.addAll(afternoonEvents);
        }

        model.addAttribute("seminars", seminars);
        model.addAttribute("events", morningEvents);
        model.addAttribute("showErrors", showErrors);
        model.addAttribute("errors", errors);

        return "index";
    }
    
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Seminar seminar = seminarRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid seminar Id:" + id));
        model.addAttribute("seminar", seminar);
        
        return "update-seminar";
    }
    
    @PostMapping("/update/{id}")
    public String updateSeminar(@PathVariable("id") long id, @Valid Seminar seminar, BindingResult result, Model model) {
        if (result.hasErrors()) {
            seminar.setId(id);
            return "update-seminar";
        }
        
        seminarRepository.save(seminar);

        return "redirect:/index";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteSeminar(@PathVariable("id") long id, Model model) {
        Seminar seminar = seminarRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid seminar Id:" + id));
        seminarRepository.delete(seminar);
        
        return "redirect:/index";
    }



    private ArrayList<Event>getMorningEvents(List<Seminar> remainingSeminars){
        showErrors=false;
        errors = new ArrayList();
        ArrayList<Event> morningEvents = new ArrayList();
        List<Seminar> matchedSeminars = new ArrayList();
        //remainingSeminars = (List<Seminar>) seminarRepository.findAll();

        ArrayList<Integer>[] combinations = new ArrayList[7];
        combinations[0] = new ArrayList<>(Arrays.asList(60,60,60));
        combinations[1] = new ArrayList<>(Arrays.asList(45,45,45,45));
        combinations[2] = new ArrayList<>(Arrays.asList(45,45,60,30));
        combinations[3] = new ArrayList<>(Arrays.asList(60,60,30,30));
        combinations[4] = new ArrayList<>(Arrays.asList(60,30,30,30,30));
        combinations[5] = new ArrayList<>(Arrays.asList(30,30,30,30,30,30));
        combinations[6] = new ArrayList<>(Arrays.asList(45,45,30,30,30));

        long timeInMs = 7*3600*1000; //TODO: timeZone
        int count = 0;
        for(ArrayList<Integer> durations : combinations){
            for (int duration : durations){

                Seminar seminar = findSeminarForDuration(remainingSeminars, duration);
                if(seminar != null){
                    remainingSeminars.remove(seminar);
                    matchedSeminars.add(seminar);

                }
                else{
                    matchedSeminars = new ArrayList();
                    remainingSeminars = (List<Seminar>) seminarRepository.findAll();
                    break;
                }
            }


            for(Seminar matchedSeminar : matchedSeminars){
                count += matchedSeminar.getDuration();
            }

            if(count == 180){
                break;
            }

        }

        if(count <180){
            showErrors=true;
            errors.add("Please add more seminars!");
        }
        else{
            for(Seminar matchedSeminar : matchedSeminars){
                Event event = new Event(matchedSeminar, new Time(timeInMs));
                timeInMs += matchedSeminar.getDuration() * 60 * 1000;
                morningEvents.add(event);
            }

            morningEvents.add(new Event(new Seminar("Lunch",60),new Time(timeInMs)));

        }

        return morningEvents;
    }

    private Seminar findSeminarForDuration(List<Seminar> seminars, int duration){
        for(Seminar seminar : seminars){
            if(seminar.getDuration() == duration){
                return seminar;
            }
        }

        return null;

    }

    private ArrayList<Event>getAfternoonEvents(List<Seminar> remainingSeminars){
        errors = new ArrayList();
        showErrors = false;
        int count=240;
        long timeInMs = 11*3600*1000;
        ArrayList<Event> afternoonEvents = new ArrayList();
        for(Seminar remainingSeminar : remainingSeminars){

            Event event = new Event(remainingSeminar, new Time(timeInMs));
            int duration = remainingSeminar.getDuration();
            timeInMs += duration * 60 * 1000;
            count += duration;
            afternoonEvents.add(event);

            /*if(count==240){
                break;
            }*/
        }

        if(count < 420){
            showErrors = true;
            errors.add("Please add more seminars!");
        }
        else if(count >= 420 && count < 480){
            int duration = 480 - count;
            Seminar networking = new Seminar("Networking", duration);
            Event event = new Event(networking, new Time(timeInMs));
            afternoonEvents.add(event);
        }


        return afternoonEvents;
    }

}
