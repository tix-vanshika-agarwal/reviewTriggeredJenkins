package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class PostAssignTravelPolicyBody {
    public List<Integer> employeeIDs;
    public List<Integer> departmentIDs;
    public List<Integer> groupIDs;
    public List<Integer> excludedEmployeeIDs;
    public String source;
    public boolean allEmployees;
}
