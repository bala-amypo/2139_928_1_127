package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // You need to change the port as per your server
                .servers(List.of(
                        new Server().url("https://9005.vs.amypo.ai")
                ));
        }
}


//Digital Complaint Prioritization Engine
This Digital Complaint Prioritization Engine lets users first register and log in
via JWT-secured /auth/register and /auth/login, then submit complaints
through /complaints/submit by sending a ComplaintRequest (title,
description, category, userld); the Complaintservice loads the user, uses
PriorityRuleService to look up a PriorityRule for the category (e.g., Billing,
Technical) and automatically calculates a priorityScore, saves a Complaint
with a timestamp (submittedOn) and creates/updates a ComplaintStatus
(OPEN, IN _ PROGRESS, RESOLVED) tracked over time. Users can then view their
own complaints via [complaints/user/ {userld} and the system can produce
a global prioritized list via [complaints/prioritized, using the repository query
findAllOrderByPriorityScoreDescCreatedAtAsc() so that higher-priority
complaints appear first and ties are broken by earliest submission. Status
changes are handled through [complaints/status/ {id} and full status history
is available via /status/history/ {complaintld}, while admins/support staff can
inspect or tune category scoring rules via /rules/all. A health-check servlet
SimpleEchoServlet responds on /ping with "PONG" and on [health with "0K",
and everything is documented via Swagger with JWT Bearer auth configured
for the secured endpoints.
Constraint: The root package for the project must be com.example.demo.
Project Requirements : Spring Boot + Hibernate + MySQL + JWT + Swagger
Folder Structure & Packages :