package me.ling.kipfin.timetable.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeInfoItemTest {

    TimeInfoItem timeInfoItem = new TimeInfoItem("09:30", "11:00");

    @Test
    public void getStartsTest(){
        LocalTime starts = timeInfoItem.getStartsTime();
        LocalTime expected = LocalTime.of(9, 30);
        assertEquals(starts, expected);
    }

    @Test
    public void isInRange(){
        LocalTime test1 = LocalTime.of(9, 30);
        LocalTime test2 = LocalTime.of(9, 31);
        LocalTime test3 = LocalTime.of(10, 40);
        LocalTime test4 = LocalTime.of(11, 0);
        LocalTime test5 = LocalTime.of(11, 10);

        assertTrue(this.timeInfoItem.isTimeInRange(test1));
        assertTrue(this.timeInfoItem.isTimeInRange(test2));
        assertTrue(this.timeInfoItem.isTimeInRange(test3));
        assertFalse(this.timeInfoItem.isTimeInRange(test4));
        assertFalse(this.timeInfoItem.isTimeInRange(test5));
    }

}