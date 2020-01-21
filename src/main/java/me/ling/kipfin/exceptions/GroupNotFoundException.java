package me.ling.kipfin.exceptions;

public class GroupNotFoundException extends TimetableException {
    public GroupNotFoundException(String g){
        super("Группа [ " + g + " ] не найдена!");
    }
    private GroupNotFoundException(Integer g){
        super("Группа с идентификатором [ " + g + " ] не найдена!");
    }
}
