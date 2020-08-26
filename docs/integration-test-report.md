# Kubernetes Integration Test Report

## Summary
TODO

## Failures  
| Test | Failure Cause
| -----|--------------
| `apiserver/TestListResourceVersion0/watchCacheOff` | [Paging in KV/range](https://github.com/gridgain-solutions/ignite-in-k8s/issues/24)
| `apiserver/TestAPIListChunking` | [Paging in KV/range](https://github.com/gridgain-solutions/ignite-in-k8s/issues/24)
| `apiserver/TestMetadataClient` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/TestAPICRDProtobuf` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/TestTransform` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/apply/TestApplyCRDNoSchema` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/apply/TestApplyCRDStructuralSchema` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/apply/TestApplyCRDNonStructuralSchema` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/apply/TestApplyCRDUnhandledSchema` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/apply/TestCreateVeryLargeObject` | [Updating too large an object succeeds but should fail](https://github.com/gridgain-solutions/ignite-in-k8s/issues/38)
| `apiserver/apply/TestUpdateVeryLargeObject` | [Updating too large an object succeeds but should fail](https://github.com/gridgain-solutions/ignite-in-k8s/issues/38)
| `apiserver/apply/TestPatchVeryLargeObject` | [Updating too large an object succeeds but should fail](https://github.com/gridgain-solutions/ignite-in-k8s/issues/38)
| `auth/TestNodeAuthorizer` | [Intermittent test failure: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `certificates/TestCSRSignerNameApprovalPlugin` | [Intermittent test failure: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `certificates/TestCSRSignerNameSigningPlugin` | [Intermittent test failure: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `client/TestAtomicPut` | [PUT not atomic](https://github.com/gridgain-solutions/ignite-in-k8s/issues/40)
| `client/TestSingleWatch` | [Watch expires](https://github.com/gridgain-solutions/ignite-in-k8s/issues/41)

## Report
```
=== RUN   TestWatchRestartsIfTimeoutNotReached
=== RUN   TestWatchRestartsIfTimeoutNotReached/regular_watcher_should_fail
=== RUN   TestWatchRestartsIfTimeoutNotReached/RetryWatcher_survives_closed_watches
=== RUN   TestWatchRestartsIfTimeoutNotReached/InformerWatcher_survives_closed_watches
--- PASS: TestWatchRestartsIfTimeoutNotReached (300.10s)
    --- PASS: TestWatchRestartsIfTimeoutNotReached/regular_watcher_should_fail (55.67s)
    --- PASS: TestWatchRestartsIfTimeoutNotReached/RetryWatcher_survives_closed_watches (120.01s)
    --- PASS: TestWatchRestartsIfTimeoutNotReached/InformerWatcher_survives_closed_watches (120.02s)
=== RUN   TestWatchClientTimeout
=== RUN   TestWatchClientTimeout/direct
=== RUN   TestWatchClientTimeout/direct/timeout
=== RUN   TestWatchClientTimeout/direct/timeoutSeconds
=== RUN   TestWatchClientTimeout/direct/timeout+timeoutSeconds
=== RUN   TestWatchClientTimeout/reverse_proxy
=== RUN   TestWatchClientTimeout/reverse_proxy/timeout
=== RUN   TestWatchClientTimeout/reverse_proxy/timeoutSeconds
=== RUN   TestWatchClientTimeout/reverse_proxy/timeout+timeoutSeconds
=== RUN   TestWatchClientTimeout/kubectl_proxy
=== RUN   TestWatchClientTimeout/kubectl_proxy/timeout
=== RUN   TestWatchClientTimeout/kubectl_proxy/timeoutSeconds
=== RUN   TestWatchClientTimeout/kubectl_proxy/timeout+timeoutSeconds
--- PASS: TestWatchClientTimeout (17.16s)
    --- PASS: TestWatchClientTimeout/direct (3.01s)
        --- PASS: TestWatchClientTimeout/direct/timeout (1.00s)
        --- PASS: TestWatchClientTimeout/direct/timeoutSeconds (1.00s)
        --- PASS: TestWatchClientTimeout/direct/timeout+timeoutSeconds (1.00s)
    --- PASS: TestWatchClientTimeout/reverse_proxy (6.01s)
        --- PASS: TestWatchClientTimeout/reverse_proxy/timeout (2.00s)
        --- PASS: TestWatchClientTimeout/reverse_proxy/timeoutSeconds (2.01s)
        --- PASS: TestWatchClientTimeout/reverse_proxy/timeout+timeoutSeconds (2.00s)
    --- PASS: TestWatchClientTimeout/kubectl_proxy (3.62s)
        --- PASS: TestWatchClientTimeout/kubectl_proxy/timeout (1.21s)
        --- PASS: TestWatchClientTimeout/kubectl_proxy/timeoutSeconds (1.21s)
        --- PASS: TestWatchClientTimeout/kubectl_proxy/timeout+timeoutSeconds (1.21s)
=== RUN   Test4xxStatusCodeInvalidPatch
--- PASS: Test4xxStatusCodeInvalidPatch (5.01s)
=== RUN   TestCacheControl
=== RUN   TestCacheControl//
=== RUN   TestCacheControl//healthz
=== RUN   TestCacheControl//openapi/v2
=== RUN   TestCacheControl//api
=== RUN   TestCacheControl//api/v1
=== RUN   TestCacheControl//apis
=== RUN   TestCacheControl//apis/apps
=== RUN   TestCacheControl//apis/apps/v1
=== RUN   TestCacheControl//api/v1/namespaces
=== RUN   TestCacheControl//apis/apps/v1/deployments
--- PASS: TestCacheControl (5.53s)
    --- PASS: TestCacheControl// (0.00s)
    --- PASS: TestCacheControl//healthz (0.00s)
    --- PASS: TestCacheControl//openapi/v2 (0.02s)
    --- PASS: TestCacheControl//api (0.00s)
    --- PASS: TestCacheControl//api/v1 (0.00s)
    --- PASS: TestCacheControl//apis (0.00s)
    --- PASS: TestCacheControl//apis/apps (0.00s)
    --- PASS: TestCacheControl//apis/apps/v1 (0.00s)
    --- PASS: TestCacheControl//api/v1/namespaces (0.01s)
    --- PASS: TestCacheControl//apis/apps/v1/deployments (0.00s)
=== RUN   Test202StatusCode
--- PASS: Test202StatusCode (5.82s)
=== RUN   TestListResourceVersion0
=== RUN   TestListResourceVersion0/watchCacheOn
=== RUN   TestListResourceVersion0/watchCacheOff
--- FAIL: TestListResourceVersion0 (10.62s)
    --- PASS: TestListResourceVersion0/watchCacheOn (5.49s)
    --- FAIL: TestListResourceVersion0/watchCacheOff (5.13s)
=== RUN   TestAPIListChunking
--- FAIL: TestAPIListChunking (5.11s)
=== RUN   TestNameInFieldSelector
--- PASS: TestNameInFieldSelector (5.67s)
=== RUN   TestMetadataClient
--- FAIL: TestMetadataClient (11.89s)
=== RUN   TestAPICRDProtobuf
--- FAIL: TestAPICRDProtobuf (36.53s)
=== RUN   TestTransform
--- FAIL: TestTransform (36.29s)
=== RUN   TestMaxJSONPatchOperations
--- PASS: TestMaxJSONPatchOperations (6.95s)
=== RUN   TestMaxResourceSize
=== RUN   TestMaxResourceSize/Create_should_limit_the_request_body_size
=== RUN   TestMaxResourceSize/Update_should_limit_the_request_body_size
=== RUN   TestMaxResourceSize/Patch_should_limit_the_request_body_size
=== RUN   TestMaxResourceSize/JSONPatchType_should_handle_a_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/JSONPatchType_should_handle_a_valid_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/MergePatchType_should_handle_a_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/MergePatchType_should_handle_a_valid_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/StrategicMergePatchType_should_handle_a_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/StrategicMergePatchType_should_handle_a_valid_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/ApplyPatchType_should_handle_a_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/ApplyPatchType_should_handle_a_valid_patch_just_under_the_max_limit
=== RUN   TestMaxResourceSize/Delete_should_limit_the_request_body_size
=== RUN   TestMaxResourceSize/create_should_limit_yaml_parsing
=== RUN   TestMaxResourceSize/create_should_handle_a_yaml_document_just_under_the_maximum_size_with_correct_nesting
=== RUN   TestMaxResourceSize/create_should_handle_a_yaml_document_just_under_the_maximum_size_with_unbalanced_nesting
=== RUN   TestMaxResourceSize/create_should_limit_json_parsing
=== RUN   TestMaxResourceSize/create_should_handle_a_json_document_just_under_the_maximum_size_with_correct_nesting
=== RUN   TestMaxResourceSize/create_should_handle_a_json_document_just_under_the_maximum_size_with_unbalanced_nesting
--- PASS: TestMaxResourceSize (20.79s)
    --- PASS: TestMaxResourceSize/Create_should_limit_the_request_body_size (0.01s)
    --- PASS: TestMaxResourceSize/Update_should_limit_the_request_body_size (0.01s)
    --- PASS: TestMaxResourceSize/Patch_should_limit_the_request_body_size (0.01s)
    --- PASS: TestMaxResourceSize/JSONPatchType_should_handle_a_patch_just_under_the_max_limit (4.86s)
    --- PASS: TestMaxResourceSize/JSONPatchType_should_handle_a_valid_patch_just_under_the_max_limit (0.14s)
    --- PASS: TestMaxResourceSize/MergePatchType_should_handle_a_patch_just_under_the_max_limit (1.63s)
    --- PASS: TestMaxResourceSize/MergePatchType_should_handle_a_valid_patch_just_under_the_max_limit (0.10s)
    --- PASS: TestMaxResourceSize/StrategicMergePatchType_should_handle_a_patch_just_under_the_max_limit (3.97s)
    --- PASS: TestMaxResourceSize/StrategicMergePatchType_should_handle_a_valid_patch_just_under_the_max_limit (0.71s)
    --- PASS: TestMaxResourceSize/ApplyPatchType_should_handle_a_patch_just_under_the_max_limit (0.05s)
    --- PASS: TestMaxResourceSize/ApplyPatchType_should_handle_a_valid_patch_just_under_the_max_limit (0.24s)
    --- PASS: TestMaxResourceSize/Delete_should_limit_the_request_body_size (0.02s)
    --- PASS: TestMaxResourceSize/create_should_limit_yaml_parsing (0.02s)
    --- PASS: TestMaxResourceSize/create_should_handle_a_yaml_document_just_under_the_maximum_size_with_correct_nesting (0.15s)
    --- PASS: TestMaxResourceSize/create_should_handle_a_yaml_document_just_under_the_maximum_size_with_unbalanced_nesting (0.07s)
    --- PASS: TestMaxResourceSize/create_should_limit_json_parsing (0.04s)
    --- PASS: TestMaxResourceSize/create_should_handle_a_json_document_just_under_the_maximum_size_with_correct_nesting (0.26s)
    --- PASS: TestMaxResourceSize/create_should_handle_a_json_document_just_under_the_maximum_size_with_unbalanced_nesting (0.08s)
=== RUN   TestPatchConflicts
--- PASS: TestPatchConflicts (26.52s)
=== RUN   TestServerSidePrint
--- PASS: TestServerSidePrint (10.92s)
=== RUN   TestWebhookAdmissionWithWatchCache
=== RUN   TestApplyCRDNoSchema
--- FAIL: TestApplyCRDNoSchema (33.34s)
=== RUN   TestApplyCRDStructuralSchema
--- FAIL: TestApplyCRDStructuralSchema (32.60s)
=== RUN   TestApplyCRDNonStructuralSchema
--- FAIL: TestApplyCRDNonStructuralSchema (32.08s)
=== RUN   TestApplyCRDUnhandledSchema
--- FAIL: TestApplyCRDUnhandledSchema (31.71s)
=== RUN   TestApplyAlsoCreates
--- PASS: TestApplyAlsoCreates (5.74s)
=== RUN   TestNoOpUpdateSameResourceVersion
--- PASS: TestNoOpUpdateSameResourceVersion (8.03s)
=== RUN   TestCreateOnApplyFailsWithUID
--- PASS: TestCreateOnApplyFailsWithUID (9.07s)
=== RUN   TestApplyUpdateApplyConflictForced
--- PASS: TestApplyUpdateApplyConflictForced (6.68s)
=== RUN   TestApplyGroupsManySeparateUpdates
--- PASS: TestApplyGroupsManySeparateUpdates (6.00s)
=== RUN   TestCreateVeryLargeObject
--- FAIL: TestCreateVeryLargeObject (7.62s)
=== RUN   TestUpdateVeryLargeObject
--- FAIL: TestUpdateVeryLargeObject (8.66s)
=== RUN   TestPatchVeryLargeObject
--- FAIL: TestPatchVeryLargeObject (21.97s)
=== RUN   TestApplyManagedFields
--- PASS: TestApplyManagedFields (8.32s)
=== RUN   TestApplyRemovesEmptyManagedFields
--- PASS: TestApplyRemovesEmptyManagedFields (9.24s)
=== RUN   TestApplyRequiresFieldManager
--- PASS: TestApplyRequiresFieldManager (10.58s)
=== RUN   TestApplyRemoveContainerPort
--- PASS: TestApplyRemoveContainerPort (5.94s)
=== RUN   TestApplyFailsWithVersionMismatch
--- PASS: TestApplyFailsWithVersionMismatch (5.63s)
=== RUN   TestApplyConvertsManagedFieldsVersion
--- PASS: TestApplyConvertsManagedFieldsVersion (5.51s)
=== RUN   TestClearManagedFieldsWithMergePatch
--- PASS: TestClearManagedFieldsWithMergePatch (5.51s)
=== RUN   TestClearManagedFieldsWithStrategicMergePatch
--- PASS: TestClearManagedFieldsWithStrategicMergePatch (5.43s)
=== RUN   TestClearManagedFieldsWithJSONPatch
--- PASS: TestClearManagedFieldsWithJSONPatch (5.78s)
=== RUN   TestClearManagedFieldsWithUpdate
--- PASS: TestClearManagedFieldsWithUpdate (6.73s)
=== RUN   TestErrorsDontFail
--- PASS: TestErrorsDontFail (6.37s)
=== RUN   TestErrorsDontFailUpdate
--- PASS: TestErrorsDontFailUpdate (9.04s)
=== RUN   TestErrorsDontFailPatch
--- PASS: TestErrorsDontFailPatch (9.79s)
=== RUN   TestClearManagedFieldsWithUpdateEmptyList
--- PASS: TestClearManagedFieldsWithUpdateEmptyList (8.11s)
=== RUN   TestApplyStatus
--- PASS: TestApplyStatus (13.02s)
    --- PASS: TestApplyStatus//v1,_Resource=namespaces (0.05s)
    --- PASS: TestApplyStatus//v1,_Resource=nodes (0.05s)
    --- PASS: TestApplyStatus//v1,_Resource=persistentvolumeclaims (0.06s)
    --- PASS: TestApplyStatus//v1,_Resource=persistentvolumes (0.04s)
    --- PASS: TestApplyStatus//v1,_Resource=pods (0.07s)
    --- PASS: TestApplyStatus//v1,_Resource=replicationcontrollers (0.05s)
    --- PASS: TestApplyStatus//v1,_Resource=resourcequotas (0.04s)
    --- PASS: TestApplyStatus//v1,_Resource=services (0.09s)
    --- SKIP: TestApplyStatus/apiregistration.k8s.io/v1,_Resource=apiservices (0.00s)
    --- SKIP: TestApplyStatus/apiregistration.k8s.io/v1beta1,_Resource=apiservices (0.00s)
    --- PASS: TestApplyStatus/extensions/v1beta1,_Resource=ingresses (0.04s)
    --- PASS: TestApplyStatus/apps/v1,_Resource=daemonsets (0.05s)
    --- PASS: TestApplyStatus/apps/v1,_Resource=deployments (0.05s)
    --- PASS: TestApplyStatus/apps/v1,_Resource=replicasets (0.04s)
    --- PASS: TestApplyStatus/apps/v1,_Resource=statefulsets (0.05s)
    --- PASS: TestApplyStatus/autoscaling/v1,_Resource=horizontalpodautoscalers (0.05s)
    --- PASS: TestApplyStatus/autoscaling/v2beta1,_Resource=horizontalpodautoscalers (0.05s)
    --- PASS: TestApplyStatus/autoscaling/v2beta2,_Resource=horizontalpodautoscalers (0.05s)
    --- PASS: TestApplyStatus/batch/v1,_Resource=jobs (0.06s)
    --- PASS: TestApplyStatus/batch/v1beta1,_Resource=cronjobs (0.07s)
    --- PASS: TestApplyStatus/batch/v2alpha1,_Resource=cronjobs (0.09s)
    --- PASS: TestApplyStatus/certificates.k8s.io/v1beta1,_Resource=certificatesigningrequests (0.07s)
    --- PASS: TestApplyStatus/networking.k8s.io/v1beta1,_Resource=ingresses (0.05s)
    --- PASS: TestApplyStatus/policy/v1beta1,_Resource=poddisruptionbudgets (0.05s)
    --- PASS: TestApplyStatus/storage.k8s.io/v1,_Resource=volumeattachments (0.06s)
    --- PASS: TestApplyStatus/apiextensions.k8s.io/v1,_Resource=customresourcedefinitions (0.08s)
    --- PASS: TestApplyStatus/apiextensions.k8s.io/v1beta1,_Resource=customresourcedefinitions (0.20s)
    --- PASS: TestApplyStatus/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=flowschemas (0.15s)
    --- PASS: TestApplyStatus/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=prioritylevelconfigurations (0.09s)
    --- PASS: TestApplyStatus/awesome.bears.com/v3,_Resource=pandas (0.22s)
    --- PASS: TestApplyStatus/awesome.bears.com/v1,_Resource=pandas (0.05s)
=== RUN   TestClientCA
--- PASS: TestClientCA (23.15s)
=== RUN   TestServingCert
--- PASS: TestServingCert (10.87s)
=== RUN   TestSNICert
--- PASS: TestSNICert (10.82s)
=== RUN   TestInsecurePodLogs
--- PASS: TestInsecurePodLogs (6.91s)
=== RUN   TestSubjectAccessReview
--- PASS: TestSubjectAccessReview (3.78s)
=== RUN   TestSelfSubjectAccessReview
--- PASS: TestSelfSubjectAccessReview (3.71s)
=== RUN   TestLocalSubjectAccessReview
--- PASS: TestLocalSubjectAccessReview (3.88s)
=== RUN   TestAuthModeAlwaysAllow
--- PASS: TestAuthModeAlwaysAllow (4.12s)
=== RUN   TestAuthModeAlwaysDeny
--- PASS: TestAuthModeAlwaysDeny (3.96s)
=== RUN   TestAliceNotForbiddenOrUnauthorized
--- PASS: TestAliceNotForbiddenOrUnauthorized (4.15s)
=== RUN   TestBobIsForbidden
--- PASS: TestBobIsForbidden (3.82s)
=== RUN   TestUnknownUserIsUnauthorized
--- PASS: TestUnknownUserIsUnauthorized (4.40s)
=== RUN   TestImpersonateIsForbidden
--- PASS: TestImpersonateIsForbidden (4.43s)
=== RUN   TestAuthorizationAttributeDetermination
--- PASS: TestAuthorizationAttributeDetermination (3.76s)
=== RUN   TestNamespaceAuthorization
--- PASS: TestNamespaceAuthorization (4.16s)
=== RUN   TestKindAuthorization
--- PASS: TestKindAuthorization (3.98s)
=== RUN   TestReadOnlyAuthorization
--- PASS: TestReadOnlyAuthorization (3.91s)
=== RUN   TestWebhookTokenAuthenticator
--- PASS: TestWebhookTokenAuthenticator (4.17s)
=== RUN   TestWebhookTokenAuthenticatorCustomDial
--- PASS: TestWebhookTokenAuthenticatorCustomDial (4.12s)
=== RUN   TestBootstrapTokenAuth
--- PASS: TestBootstrapTokenAuth (11.69s)
=== RUN   TestDynamicClientBuilder
--- PASS: TestDynamicClientBuilder (14.67s)
=== RUN   TestNodeAuthorizer
--- FAIL: TestNodeAuthorizer (31.64s)
=== RUN   TestRBAC
--- PASS: TestRBAC (30.25s)
=== RUN   TestBootstrapping
--- PASS: TestBootstrapping (6.28s)
=== RUN   TestDiscoveryUpgradeBootstrapping
--- PASS: TestDiscoveryUpgradeBootstrapping (8.33s)
=== RUN   TestServiceAccountTokenCreate
=== RUN   TestServiceAccountTokenCreate/bound_to_service_account
=== RUN   TestServiceAccountTokenCreate/bound_to_service_account_and_pod
=== RUN   TestServiceAccountTokenCreate/bound_to_service_account_and_secret
=== RUN   TestServiceAccountTokenCreate/bound_to_service_account_and_pod_running_as_different_service_account
=== RUN   TestServiceAccountTokenCreate/expired_token
=== RUN   TestServiceAccountTokenCreate/a_token_without_an_api_audience_is_invalid
=== RUN   TestServiceAccountTokenCreate/a_tokenrequest_without_an_audience_is_valid_against_the_api
=== RUN   TestServiceAccountTokenCreate/a_token_should_be_invalid_after_recreating_same_name_pod
=== RUN   TestServiceAccountTokenCreate/a_token_should_be_invalid_after_recreating_same_name_secret
=== RUN   TestServiceAccountTokenCreate/a_token_request_within_expiration_time
=== RUN   TestServiceAccountTokenCreate/a_token_request_with_out-of-range_expiration
=== RUN   TestServiceAccountTokenCreate/a_token_is_valid_against_the_HTTP-provided_service_account_issuer_metadata
--- PASS: TestServiceAccountTokenCreate (8.10s)
    --- PASS: TestServiceAccountTokenCreate/bound_to_service_account (0.09s)
    --- PASS: TestServiceAccountTokenCreate/bound_to_service_account_and_pod (0.29s)
    --- PASS: TestServiceAccountTokenCreate/bound_to_service_account_and_secret (0.19s)
    --- PASS: TestServiceAccountTokenCreate/bound_to_service_account_and_pod_running_as_different_service_account (0.06s)
    --- PASS: TestServiceAccountTokenCreate/expired_token (0.03s)
    --- PASS: TestServiceAccountTokenCreate/a_token_without_an_api_audience_is_invalid (0.03s)
    --- PASS: TestServiceAccountTokenCreate/a_tokenrequest_without_an_audience_is_valid_against_the_api (0.02s)
    --- PASS: TestServiceAccountTokenCreate/a_token_should_be_invalid_after_recreating_same_name_pod (0.11s)
    --- PASS: TestServiceAccountTokenCreate/a_token_should_be_invalid_after_recreating_same_name_secret (0.08s)
    --- PASS: TestServiceAccountTokenCreate/a_token_request_within_expiration_time (0.09s)
    --- PASS: TestServiceAccountTokenCreate/a_token_request_with_out-of-range_expiration (0.28s)
    --- PASS: TestServiceAccountTokenCreate/a_token_is_valid_against_the_HTTP-provided_service_account_issuer_metadata (0.02s)
=== RUN   TestCSRSignerNameApprovalPlugin
=== RUN   TestCSRSignerNameApprovalPlugin/should_admit_when_a_user_has_permission_for_the_exact_signerName
=== RUN   TestCSRSignerNameApprovalPlugin/should_admit_when_a_user_has_permission_for_the_wildcard-suffixed_signerName
=== RUN   TestCSRSignerNameApprovalPlugin/should_deny_if_a_user_does_not_have_permission_for_the_given_signerName
--- FAIL: TestCSRSignerNameApprovalPlugin (50.56s)
    --- FAIL: TestCSRSignerNameApprovalPlugin/should_admit_when_a_user_has_permission_for_the_exact_signerName (33.44s)
    --- PASS: TestCSRSignerNameApprovalPlugin/should_admit_when_a_user_has_permission_for_the_wildcard-suffixed_signerName (8.74s)
    --- PASS: TestCSRSignerNameApprovalPlugin/should_deny_if_a_user_does_not_have_permission_for_the_given_signerName (8.38s)
=== RUN   TestCSRSignerNameSigningPlugin
=== RUN   TestCSRSignerNameSigningPlugin/should_admit_when_a_user_has_permission_for_the_exact_signerName
=== RUN   TestCSRSignerNameSigningPlugin/should_admit_when_a_user_has_permission_for_the_wildcard-suffixed_signerName
=== RUN   TestCSRSignerNameSigningPlugin/should_deny_if_a_user_does_not_have_permission_for_the_given_signerName
--- FAIL: TestCSRSignerNameSigningPlugin (50.39s)
    --- PASS: TestCSRSignerNameSigningPlugin/should_admit_when_a_user_has_permission_for_the_exact_signerName (9.65s)
    --- PASS: TestCSRSignerNameSigningPlugin/should_admit_when_a_user_has_permission_for_the_wildcard-suffixed_signerName (8.74s)
    --- FAIL: TestCSRSignerNameSigningPlugin/should_deny_if_a_user_does_not_have_permission_for_the_given_signerName (32.00s)
=== RUN   TestCertificateSubjectRestrictionPlugin
=== RUN   TestCertificateSubjectRestrictionPlugin/should_admit_a_request_if_signerName_is_NOT_kube-apiserver-client_and_org_is_system:masters
=== RUN   TestCertificateSubjectRestrictionPlugin/should_admit_a_request_if_signerName_is_kube-apiserver-client_and_group_is_NOT_system:masters
=== RUN   TestCertificateSubjectRestrictionPlugin/should_reject_a_request_if_signerName_is_kube-apiserver-client_and_group_is_system:masters
--- PASS: TestCertificateSubjectRestrictionPlugin (23.76s)
    --- PASS: TestCertificateSubjectRestrictionPlugin/should_admit_a_request_if_signerName_is_kube-apiserver-client_and_group_is_NOT_system:masters (7.84s)
    --- PASS: TestCertificateSubjectRestrictionPlugin/should_reject_a_request_if_signerName_is_kube-apiserver-client_and_group_is_system:masters (7.81s)
    --- PASS: TestCertificateSubjectRestrictionPlugin/should_admit_a_request_if_signerName_is_NOT_kube-apiserver-client_and_org_is_system:masters (8.11s)
=== RUN   TestController_AutoApproval
=== RUN   TestController_AutoApproval/should_auto-approve_CSR_that_has_kube-apiserver-client-kubelet_signerName_and_matches_requirements
=== RUN   TestController_AutoApproval/should_auto-approve_CSR_that_has_kube-apiserver-client-kubelet_signerName_and_matches_requirements_despite_missing_username_if_nodeclient_permissions_are_granted
=== RUN   TestController_AutoApproval/should_not_auto-approve_CSR_that_has_kube-apiserver-client-kubelet_signerName_that_does_not_match_requirements
=== RUN   TestController_AutoApproval/should_not_auto-approve_CSR_that_has_kube-apiserver-client_signerName_that_DOES_match_kubelet_CSR_requirements
--- FAIL: TestController_AutoApproval (137.36s)
    --- FAIL: TestController_AutoApproval/should_auto-approve_CSR_that_has_kube-apiserver-client-kubelet_signerName_and_matches_requirements (37.33s)
    --- FAIL: TestController_AutoApproval/should_auto-approve_CSR_that_has_kube-apiserver-client-kubelet_signerName_and_matches_requirements_despite_missing_username_if_nodeclient_permissions_are_granted (34.47s)
    --- FAIL: TestController_AutoApproval/should_not_auto-approve_CSR_that_has_kube-apiserver-client-kubelet_signerName_that_does_not_match_requirements (32.71s)
    --- FAIL: TestController_AutoApproval/should_not_auto-approve_CSR_that_has_kube-apiserver-client_signerName_that_DOES_match_kubelet_CSR_requirements (32.85s)
=== RUN   TestCSRSignerNameDefaulting
=== RUN   TestCSRSignerNameDefaulting/defaults_to_legacy-unknown_if_not_recognised
=== RUN   TestCSRSignerNameDefaulting/does_not_default_signerName_if_an_explicit_value_is_provided
--- PASS: TestCSRSignerNameDefaulting (8.75s)
    --- PASS: TestCSRSignerNameDefaulting/defaults_to_legacy-unknown_if_not_recognised (4.33s)
    --- PASS: TestCSRSignerNameDefaulting/does_not_default_signerName_if_an_explicit_value_is_provided (4.42s)
=== RUN   TestCSRSignerNameFieldSelector
--- PASS: TestCSRSignerNameFieldSelector (4.87s)
=== RUN   TestCertRotation
--- PASS: TestCertRotation (48.51s)
=== RUN   TestCertRotationContinuousRequests
--- PASS: TestCertRotationContinuousRequests (48.31s)
=== RUN   TestClient
--- PASS: TestClient (7.69s)
=== RUN   TestAtomicPut
--- FAIL: TestAtomicPut (31.60s)
=== RUN   TestPatch
--- PASS: TestPatch (7.56s)
=== RUN   TestPatchWithCreateOnUpdate
--- PASS: TestPatchWithCreateOnUpdate (7.76s)
=== RUN   TestAPIVersions
--- PASS: TestAPIVersions (7.61s)
=== RUN   TestSingleWatch
--- FAIL: TestSingleWatch (32.66s)
=== RUN   TestMultiWatch
=== RUN   TestSelfLinkOnNamespace
--- FAIL: TestSelfLinkOnNamespace (34.62s)
=== RUN   TestDynamicClient
--- FAIL: TestDynamicClient (37.34s)
=== RUN   TestDynamicClientWatch
--- FAIL: TestDynamicClientWatch (38.35s)
=== RUN   TestConfigMap
--- PASS: TestConfigMap (3.84s)
=== RUN   TestCronJobLaunchesPodAndCleansUp
--- PASS: TestCronJobLaunchesPodAndCleansUp (65.22s)
=== RUN   TestOneNodeDaemonLaunchesPod
=== RUN   TestOneNodeDaemonLaunchesPod/TestOneNodeDaemonLaunchesPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestOneNodeDaemonLaunchesPod/TestOneNodeDaemonLaunchesPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- FAIL: TestOneNodeDaemonLaunchesPod (128.25s)
    --- FAIL: TestOneNodeDaemonLaunchesPod/TestOneNodeDaemonLaunchesPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (63.97s)
    --- FAIL: TestOneNodeDaemonLaunchesPod/TestOneNodeDaemonLaunchesPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (64.28s)
=== RUN   TestSimpleDaemonSetLaunchesPods
=== RUN   TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- FAIL: TestSimpleDaemonSetLaunchesPods (130.03s)
    --- FAIL: TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (65.05s)
    --- FAIL: TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (64.98s)
=== RUN   TestDaemonSetWithNodeSelectorLaunchesPods
=== RUN   TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- FAIL: TestDaemonSetWithNodeSelectorLaunchesPods (84.22s)
    --- FAIL: TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (64.88s)
    --- PASS: TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (19.33s)
=== RUN   TestNotReadyNodeDaemonDoesLaunchPod
=== RUN   TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- FAIL: TestNotReadyNodeDaemonDoesLaunchPod (129.34s)
    --- FAIL: TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (64.35s)
    --- FAIL: TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (64.99s)
=== RUN   TestInsufficientCapacityNode
=== RUN   TestInsufficientCapacityNode/TestInsufficientCapacityNode_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestInsufficientCapacityNode/TestInsufficientCapacityNode_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
=== RUN   TestAdmission
--- PASS: TestAdmission (3.95s)
=== RUN   TestNewDeployment
--- FAIL: TestNewDeployment (64.69s)
=== RUN   TestDeploymentRollingUpdate
--- FAIL: TestDeploymentRollingUpdate (65.24s)
=== RUN   TestDeploymentSelectorImmutability
--- PASS: TestDeploymentSelectorImmutability (5.01s)
=== RUN   TestPausedDeployment
--- FAIL: TestPausedDeployment (65.49s)
=== RUN   TestScalePausedDeployment
--- FAIL: TestScalePausedDeployment (67.49s)
=== RUN   TestDeploymentHashCollision
=== RUN   TestPDBWithScaleSubresource
--- FAIL: TestPDBWithScaleSubresource (33.74s)
=== RUN   TestDryRun
--- FAIL: TestDryRun (32.35s)
=== RUN   TestOverlappingBuiltInResources
--- PASS: TestOverlappingBuiltInResources (0.00s)
=== RUN   TestOverlappingCustomResourceAPIService
--- FAIL: TestOverlappingCustomResourceAPIService (65.25s)
=== RUN   TestOverlappingCustomResourceCustomResourceDefinition
--- FAIL: TestOverlappingCustomResourceCustomResourceDefinition (65.49s)
=== RUN   TestCrossGroupStorage
--- FAIL: TestCrossGroupStorage (66.81s)
=== RUN   TestEtcdStoragePath
--- FAIL: TestEtcdStoragePath (70.76s)
=== RUN   TestEventCompatibility
--- FAIL: TestEventCompatibility (34.41s)
=== RUN   TestConcurrentEvictionRequests
--- PASS: TestConcurrentEvictionRequests (9.44s)
=== RUN   TestTerminalPodEviction
--- PASS: TestTerminalPodEviction (7.18s)
=== RUN   TestAggregatedAPIServer
--- FAIL: TestAggregatedAPIServer (31.92s)
=== RUN   TestWebhookLoopback
--- PASS: TestWebhookLoopback (6.04s)
=== RUN   TestClusterScopedOwners
--- FAIL: TestClusterScopedOwners (32.57s)
=== RUN   TestCascadingDeletion
--- FAIL: TestCascadingDeletion (32.12s)
=== RUN   TestCreateWithNonExistentOwner
--- FAIL: TestCreateWithNonExistentOwner (33.73s)
=== RUN   TestStressingCascadingDeletion
--- FAIL: TestStressingCascadingDeletion (32.23s)
=== RUN   TestOrphaning
--- FAIL: TestOrphaning (33.94s)
=== RUN   TestSolidOwnerDoesNotBlockWaitingOwner
--- FAIL: TestSolidOwnerDoesNotBlockWaitingOwner (34.79s)
=== RUN   TestNonBlockingOwnerRefDoesNotBlock
--- FAIL: TestNonBlockingOwnerRefDoesNotBlock (31.76s)
=== RUN   TestDoubleDeletionWithFinalizer
--- FAIL: TestDoubleDeletionWithFinalizer (39.76s)
=== RUN   TestBlockingOwnerRefDoesBlock
--- FAIL: TestBlockingOwnerRefDoesBlock (33.28s)
=== RUN   TestCustomResourceCascadingDeletion
--- FAIL: TestCustomResourceCascadingDeletion (34.76s)
=== RUN   TestMixedRelationships
--- FAIL: TestMixedRelationships (39.30s)
=== RUN   TestCRDDeletionCascading
--- FAIL: TestCRDDeletionCascading (36.54s)
=== RUN   TestPerformance
=== RUN   TestWatchBasedManager
--- FAIL: TestWatchBasedManager (33.18s)
=== RUN   TestDynamicAudit
=== RUN   TestDynamicAudit/one_sink
--- FAIL: TestDynamicAudit (97.36s)
    --- FAIL: TestDynamicAudit/one_sink (90.08s)
=== RUN   TestAudit
=== RUN   TestAudit/audit.k8s.io/v1.RequestResponse.false
=== RUN   TestAudit/audit.k8s.io/v1.Metadata.true
=== RUN   TestAudit/audit.k8s.io/v1.Request.true
=== RUN   TestAudit/audit.k8s.io/v1.RequestResponse.true
=== RUN   TestAudit/audit.k8s.io/v1beta1.RequestResponse.false
=== RUN   TestAudit/audit.k8s.io/v1beta1.Metadata.true
=== RUN   TestAudit/audit.k8s.io/v1beta1.Request.true
=== RUN   TestAudit/audit.k8s.io/v1beta1.RequestResponse.true
--- FAIL: TestAudit (253.67s)
    --- FAIL: TestAudit/audit.k8s.io/v1.RequestResponse.false (35.86s)
    --- FAIL: TestAudit/audit.k8s.io/v1.Metadata.true (33.07s)
    --- PASS: TestAudit/audit.k8s.io/v1.Request.true (10.78s)
    --- FAIL: TestAudit/audit.k8s.io/v1.RequestResponse.true (33.95s)
    --- FAIL: TestAudit/audit.k8s.io/v1beta1.RequestResponse.false (32.87s)
    --- FAIL: TestAudit/audit.k8s.io/v1beta1.Metadata.true (39.03s)
    --- FAIL: TestAudit/audit.k8s.io/v1beta1.Request.true (34.24s)
    --- FAIL: TestAudit/audit.k8s.io/v1beta1.RequestResponse.true (33.87s)
=== RUN   TestCRDShadowGroup
--- FAIL: TestCRDShadowGroup (33.36s)
=== RUN   TestCRD
--- FAIL: TestCRD (32.15s)
=== RUN   TestCRDOpenAPI
--- FAIL: TestCRDOpenAPI (31.88s)
=== RUN   TestGracefulShutdown
--- FAIL: TestGracefulShutdown (31.81s)
=== RUN   TestKMSProvider
--- FAIL: TestKMSProvider (31.84s)
=== RUN   TestKMSHealthz
--- FAIL: TestKMSHealthz (31.95s)
=== RUN   TestRun
--- FAIL: TestRun (31.76s)
=== RUN   TestLivezAndReadyz
=== RUN   TestMasterProcessMetrics
=== RUN   TestApiserverMetrics
--- PASS: TestApiserverMetrics (4.61s)
=== RUN   TestNamespaceCondition
--- FAIL: TestNamespaceCondition (64.54s)
=== RUN   TestTaintBasedEvictions
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_200_tolerationseconds
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_with_no_pod_tolerations
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_0_tolerationseconds
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeUnreachable
--- FAIL: TestTaintBasedEvictions (74.97s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_200_tolerationseconds (11.00s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_with_no_pod_tolerations (11.63s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_0_tolerationseconds (16.06s)
    --- FAIL: TestTaintBasedEvictions/Taint_based_evictions_for_NodeUnreachable (36.28s)
=== RUN   TestIgnoreClusterName
--- PASS: TestIgnoreClusterName (4.34s)
=== RUN   TestMasterExportsSymbols
--- PASS: TestMasterExportsSymbols (0.00s)
=== RUN   TestPodUpdateActiveDeadlineSeconds
--- PASS: TestPodUpdateActiveDeadlineSeconds (7.67s)
=== RUN   TestPodReadOnlyFilesystem
--- PASS: TestPodReadOnlyFilesystem (5.43s)
=== RUN   TestPodCreateEphemeralContainers
--- PASS: TestPodCreateEphemeralContainers (4.46s)
=== RUN   TestPodPatchEphemeralContainers
--- PASS: TestPodPatchEphemeralContainers (10.37s)
=== RUN   TestPodUpdateEphemeralContainers
--- PASS: TestPodUpdateEphemeralContainers (11.04s)
=== RUN   TestQuota
--- FAIL: TestQuota (192.22s)
=== RUN   TestQuotaLimitedResourceDenial
--- FAIL: TestQuotaLimitedResourceDenial (66.02s)
=== RUN   TestAdoption
--- FAIL: TestAdoption (592.74s)
=== RUN   TestRSSelectorImmutability
--- PASS: TestRSSelectorImmutability (4.76s)
=== RUN   TestSpecReplicasChange
=== RUN   TestAdoption
--- FAIL: TestAdoption (65.38s)
=== RUN   TestSpecReplicasChange
--- PASS: TestSpecReplicasChange (11.91s)
=== RUN   TestDeletingAndFailedPods
--- PASS: TestDeletingAndFailedPods (5.65s)
=== RUN   TestOverlappingRCs
--- FAIL: TestOverlappingRCs (66.44s)
=== RUN   TestPodOrphaningAndAdoptionWhenLabelsChange
--- PASS: TestPodOrphaningAndAdoptionWhenLabelsChange (5.99s)
=== RUN   TestGeneralPodAdoption
--- PASS: TestGeneralPodAdoption (5.72s)
=== RUN   TestReadyAndAvailableReplicas
--- PASS: TestReadyAndAvailableReplicas (4.83s)
=== RUN   TestRCScaleSubresource
--- PASS: TestRCScaleSubresource (6.56s)
=== RUN   TestExtraPodsAdoptionAndDeletion
=== RUN   TestScaleSubresources
--- FAIL: TestScaleSubresources (32.12s)
=== RUN   TestSchedulerExtender
--- FAIL: TestSchedulerExtender (34.38s)
=== RUN   TestPreFilterPlugin
--- FAIL: TestPreFilterPlugin (39.95s)
=== RUN   TestScorePlugin
--- PASS: TestScorePlugin (8.19s)
=== RUN   TestNormalizeScorePlugin
--- FAIL: TestNormalizeScorePlugin (38.84s)
=== RUN   TestReservePlugin
--- PASS: TestReservePlugin (5.06s)
=== RUN   TestPrebindPlugin
--- FAIL: TestPrebindPlugin (125.12s)
=== RUN   TestUnreservePlugin
--- FAIL: TestUnreservePlugin (64.51s)
=== RUN   TestBindPlugin
--- FAIL: TestBindPlugin (274.47s)
=== RUN   TestPostBindPlugin
--- PASS: TestPostBindPlugin (4.71s)
=== RUN   TestPermitPlugin
=== RUN   TestSchedule100Node3KPods
=== RUN   TestSecrets
--- PASS: TestSecrets (4.46s)
=== RUN   TestServiceAccountAutoCreate
--- PASS: TestServiceAccountAutoCreate (9.72s)
=== RUN   TestServiceAccountTokenAutoCreate
--- PASS: TestServiceAccountTokenAutoCreate (14.44s)
=== RUN   TestServiceAccountTokenAutoMount
--- FAIL: TestServiceAccountTokenAutoMount (17.69s)
=== RUN   TestServiceAccountTokenAuthentication
--- FAIL: TestServiceAccountTokenAuthentication (17.19s)
=== RUN   TestComponentSecureServingAndAuth
--- FAIL: TestComponentSecureServingAndAuth (33.64s)
=== RUN   TestVolumeTemplateNoopUpdate
--- FAIL: TestVolumeTemplateNoopUpdate (32.01s)
=== RUN   TestSpecReplicasChange
--- FAIL: TestSpecReplicasChange (65.52s)
=== RUN   TestDeletingAndFailedPods
--- FAIL: TestDeletingAndFailedPods (64.80s)
=== RUN   TestStorageClasses
--- PASS: TestStorageClasses (4.44s)
=== RUN   TestAPICiphers
--- FAIL: TestAPICiphers (32.06s)
=== RUN   TestTTLAnnotations
--- FAIL: TestTTLAnnotations (34.72s)
=== RUN   TestPodDeletionWithDswp
--- FAIL: TestPodDeletionWithDswp (504.72s)
=== RUN   TestPodUpdateWithWithADC
--- PASS: TestPodUpdateWithWithADC (5.54s)
=== RUN   TestPodUpdateWithKeepTerminatedPodVolumes
--- PASS: TestPodUpdateWithKeepTerminatedPodVolumes (5.14s)
=== RUN   TestPodAddedByDswp
--- PASS: TestPodAddedByDswp (7.53s)
=== RUN   TestPVCBoundWithADC
--- PASS: TestPVCBoundWithADC (19.56s)
=== RUN   TestPersistentVolumeRecycler
--- PASS: TestPersistentVolumeRecycler (5.19s)
=== RUN   TestPersistentVolumeDeleter
--- PASS: TestPersistentVolumeDeleter (5.37s)
=== RUN   TestPersistentVolumeBindRace
--- PASS: TestPersistentVolumeBindRace (5.56s)
=== RUN   TestPersistentVolumeClaimLabelSelector
--- PASS: TestPersistentVolumeClaimLabelSelector (5.23s)
=== RUN   TestPersistentVolumeClaimLabelSelectorMatchExpressions
--- PASS: TestPersistentVolumeClaimLabelSelectorMatchExpressions (5.44s)
=== RUN   TestPersistentVolumeMultiPVs
--- PASS: TestPersistentVolumeMultiPVs (6.16s)
=== RUN   TestPersistentVolumeMultiPVsPVCs
=== RUN   TestVolumeBinding
--- FAIL: TestVolumeBinding (34.93s)
=== RUN   TestVolumeBindingRescheduling
--- FAIL: TestVolumeBindingRescheduling (125.52s)
=== RUN   TestVolumeBindingStress
=== RUN   TestAPIApproval
--- FAIL: TestAPIApproval (31.12s)
=== RUN   TestApplyBasic
--- FAIL: TestApplyBasic (31.67s)
=== RUN   TestServerUp
--- PASS: TestServerUp (1.07s)
=== RUN   TestNamespaceScopedCRUD
--- FAIL: TestNamespaceScopedCRUD (31.09s)
=== RUN   TestClusterScopedCRUD
--- FAIL: TestClusterScopedCRUD (31.08s)
=== RUN   TestInvalidCRUD
--- FAIL: TestInvalidCRUD (31.09s)
=== RUN   TestDiscovery
--- FAIL: TestDiscovery (31.24s)
=== RUN   TestNoNamespaceReject
--- FAIL: TestNoNamespaceReject (31.14s)
=== RUN   TestSameNameDiffNamespace
--- FAIL: TestSameNameDiffNamespace (31.18s)
=== RUN   TestSelfLink
--- FAIL: TestSelfLink (31.03s)
=== RUN   TestPreserveInt
--- FAIL: TestPreserveInt (31.12s)
=== RUN   TestPatch
--- FAIL: TestPatch (6.56s)
=== RUN   TestCrossNamespaceListWatch
--- FAIL: TestCrossNamespaceListWatch (6.61s)
=== RUN   TestNameConflict
--- FAIL: TestNameConflict (6.66s)
=== RUN   TestStatusGetAndPatch
--- FAIL: TestStatusGetAndPatch (6.61s)
=== RUN   TestChangeCRD
--- FAIL: TestChangeCRD (6.63s)
=== RUN   TestCustomResourceDefaultingWithWatchCache
--- FAIL: TestCustomResourceDefaultingWithWatchCache (6.63s)
=== RUN   TestCustomResourceDefaultingWithoutWatchCache
--- FAIL: TestCustomResourceDefaultingWithoutWatchCache (6.59s)
=== RUN   TestCustomResourceDefaultingOfMetaFields
--- FAIL: TestCustomResourceDefaultingOfMetaFields (6.59s)
=== RUN   TestFinalization
--- FAIL: TestFinalization (6.56s)
=== RUN   TestFinalizationAndDeletion
--- FAIL: TestFinalizationAndDeletion (6.65s)
=== RUN   TestLimits
--- FAIL: TestLimits (6.64s)
=== RUN   TestListTypes
--- FAIL: TestListTypes (20.37s)
=== RUN   TestWebhookConverterWithWatchCache
--- FAIL: TestWebhookConverterWithWatchCache (31.12s)
=== RUN   TestWebhookConverterWithoutWatchCache
--- FAIL: TestWebhookConverterWithoutWatchCache (31.46s)
```