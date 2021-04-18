package com.agomezlucena.youcandoit.task_managament

import java.time.LocalDate

data class TasksPerDay (val date:LocalDate, val listOfTask: List<Task>)