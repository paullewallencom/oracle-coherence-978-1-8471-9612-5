/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.samples.bank.web.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.seovic.samples.bank.domain.ExchangeRateRepository;
import com.seovic.samples.bank.domain.repository.FakeExchangeRateRepository;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Aleksandar Seovic  2010.02.02
 */
@Controller
public class ExchangeRateService
    {
    @RequestMapping(value = "/exchange-rate/{instrument}", method = GET)
    public void getExchangeRate(@PathVariable String instrument,
                                HttpServletResponse response)
            throws IOException
        {
        response.setContentType("text/plain");
        response.getWriter().write(
                m_exchangeRateRepository.getExchangeRate(instrument).toString());
        }
    
    private ExchangeRateRepository m_exchangeRateRepository =
            new FakeExchangeRateRepository();
    }
