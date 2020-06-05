/*
 * Copyright 2020 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.enterprise.authorization;

import java.util.Set;

public class AllowedUsersAndGroups {
    private final Set<String> allowedUsers;
    private final Set<String> allowedGroups;

    public AllowedUsersAndGroups(Set<String> allowedUsers, Set<String> allowedGroups) {
        this.allowedUsers = allowedUsers;
        this.allowedGroups = allowedGroups;
    }

    public Set<String> getAllowedUsers() {
        return allowedUsers;
    }

    public Set<String> getAllowedGroups() {
        return allowedGroups;
    }

    public boolean containsUser(String userId) {
        return allowedUsers.contains(userId);
    }

    public boolean isEmpty() {
        return allowedUsers.isEmpty() && allowedGroups.isEmpty();
    }
}
