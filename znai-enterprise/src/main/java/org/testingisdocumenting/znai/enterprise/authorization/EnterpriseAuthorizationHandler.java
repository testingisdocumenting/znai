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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.enterprise.DocLifecycleListener;
import org.testingisdocumenting.znai.enterprise.authorization.groups.AuthorizationGroupResolutionServices;
import org.testingisdocumenting.znai.server.auth.AuthorizationHandler;
import org.testingisdocumenting.znai.server.auth.AuthorizationRequestLink;
import org.testingisdocumenting.znai.core.DocMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.documentationStorage;
import static org.testingisdocumenting.znai.enterprise.EnterpriseComponentsRegistry.enterpriseConfig;

public class EnterpriseAuthorizationHandler implements AuthorizationHandler, DocLifecycleListener {
    private Cache<UserIdDocId, Boolean> authorizedByIds;
    private final Map<String, AllowedUsersAndGroups> allowedUsersAndGroupsById = new ConcurrentHashMap<>();

    public EnterpriseAuthorizationHandler() {
        if (disabled()) {
            return;
        }

        authorizedByIds = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
        buildAllowedUsersAndGroupsCache();
    }

    @Override
    public boolean isAuthorized(String userId, String docId) {
        if (AuthorizationGroupResolutionServices.isEmpty()) {
            ConsoleOutputs.out("AuthorizationGroupResolutionServices.isEmpty(): " +
                    "no authorization is being performed for <" + docId + ">");
            return true;
        }

        AllowedUsersAndGroups allowedUsersAndGroups = allowedUsersAndGroupsById.get(docId);
        if (allowedUsersAndGroups == null || allowedUsersAndGroups.isEmpty()) {
            ConsoleOutputs.out("allowed users and groups is empty: " +
                    "no authorization is being performed for <" + docId + ">");
            return true;
        }

        if (allowedUsersAndGroups.containsUser(userId)) {
            return true;
        }

        if (allowedUsersAndGroups.getAllowedGroups().isEmpty()) {
            return false;
        }

        Boolean present = authorizedByIds.get(new UserIdDocId(userId, docId),
                userIdDocId -> authorized(allowedUsersAndGroups, userId));

        return present != null && present;
    }

    @Override
    public List<String> allowedGroups(String docId) {
        AllowedUsersAndGroups allowedUsersAndGroups = allowedUsersAndGroupsById.get(docId);
        if (allowedUsersAndGroups == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(allowedUsersAndGroups.getAllowedGroups());
    }

    @Override
    public AuthorizationRequestLink authorizationRequestLink() {
        return enterpriseConfig().getAuthzRequestLink();
    }

    @Override
    public void onDocUpdate(DocMeta docMeta) {
        allowedUsersAndGroupsById.put(docMeta.getId(), createAllowedUsersAndGroups(docMeta));
    }

    @Override
    public void onDocRemove(String docId) {
        allowedUsersAndGroupsById.remove(docId);
    }

    private void buildAllowedUsersAndGroupsCache() {
        documentationStorage().list().forEach(this::associatedAllowedById);
    }

    private void associatedAllowedById(DocMeta docMeta) {
        allowedUsersAndGroupsById.put(docMeta.getId(), createAllowedUsersAndGroups(docMeta));
    }

    private AllowedUsersAndGroups createAllowedUsersAndGroups(DocMeta docMeta) {
        return new AllowedUsersAndGroups(
                new HashSet<>(docMeta.getAllowedUsers()),
                new HashSet<>(docMeta.getAllowedGroups()));
    }

    private Boolean authorized(AllowedUsersAndGroups allowedUsersAndGroups, String userId) {
        return allowedUsersAndGroups.getAllowedGroups().stream()
                .anyMatch(group -> inGroup(group, userId));
    }

    private boolean inGroup(String group, String userId) {
        return AuthorizationGroupResolutionServices.groupContainsUser(group, userId);
    }

    private boolean disabled() {
        return documentationStorage() == null;
    }
}
