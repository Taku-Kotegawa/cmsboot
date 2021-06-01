/*
 * Copyright (C) 2017 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.stnet.cms.example.application.job.job01;

import jp.co.stnet.cms.example.domain.model.job01.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * ItemProcessor of employees.
 */
@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    /**
     * Processing some stuff for each employee.
     *
     * @param item employee model
     * @return processed employee model.
     */
    @Override
    public Employee process(Employee item) {
        // To upper case employee name.
        item.setEmpName(item.getEmpName().toUpperCase());
        return item;
    }
}
