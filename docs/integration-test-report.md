# Kubernetes Integration Test Report

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
| `auth/TestNodeAuthorizer` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `certificates/TestCSRSignerNameApprovalPlugin` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `certificates/TestCSRSignerNameSigningPlugin` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `certificates/TestController_AutoApproval` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `client/TestAtomicPut` | [PUT not atomic](https://github.com/gridgain-solutions/ignite-in-k8s/issues/40)
| `client/TestSingleWatch` | [Watch took longer than TIMEOUT](https://github.com/gridgain-solutions/ignite-in-k8s/issues/41)
| `client/TestSelfLinkOnNamespace` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `client/TestDynamicClient` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `client/TestDynamicClientWatch` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `daemonset/TestSimpleDaemonSetLaunchesPods` | [Apiserver received an error that is not an metav1.Status](https://github.com/gridgain-solutions/ignite-in-k8s/issues/43)
| `disruption/TestPDBWithScaleSubresource` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `etcd/TestCrossGroupStorage` | [Timed out waiting for watch event for v1 in response to persisting v1](https://github.com/gridgain-solutions/ignite-in-k8s/issues/44)
| `events/TestEventCompatibility` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `examples/TestAggregatedAPIServer` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `garbagecollector/TestClusterScopedOwners` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `garbagecollector/TestStressingCascadingDeletion` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `garbagecollector/TestOrphaning` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `garbagecollector/TestCustomResourceCascadingDeletion` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `garbagecollector/TestMixedRelationships` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `garbagecollector/TestCRDDeletionCascading` | [Failed to create CustomResourceDefinition: gave up waiting for watch event](https://github.com/gridgain-solutions/ignite-in-k8s/issues/45)
| `kubelet/TestWatchBasedManager` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `master/TestAudit` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `master/TestLivezAndReadyz` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `master/TestReconcilerMasterLeaseMultiCombined` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `master/TestSecretsShouldBeTransformed` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `quota/TestQuota` | [Timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/46)
| `scale/TestScaleSubresources` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `scheduler/TestPreemptionRaces` | [Pod didn't get scheduled: timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/47)
| `scheduler/TestNominatedNodeCleanUp` | [Pod didn't get scheduled: timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/47)
| `serviceaccount/TestServiceAccountTokenAutoMount` | [Timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/46)
| `serving/TestComponentSecureServingAndAuth` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `statefulset/TestVolumeTemplateNoopUpdate` | [Failed to launch server: failed to wait for /healthz to return ok](https://github.com/gridgain-solutions/ignite-in-k8s/issues/39)
| `ttlcontroller/TestTTLAnnotations` | [Timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/46)
| `volume/TestPersistentVolumeMultiPVsPVCs` | [Test timed out after 10 minutes](https://github.com/gridgain-solutions/ignite-in-k8s/issues/48)
| `volumescheduling/TestVolumeBinding` | [Timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/46)
| `volumescheduling/TestVolumeBindingStressWithSchedulerResync` | [Timed out waiting for the condition](https://github.com/gridgain-solutions/ignite-in-k8s/issues/46)
| `volumescheduling/TestPersistentVolumeMultiPVsPVCs` | [Test timed out after 10 minutes](https://github.com/gridgain-solutions/ignite-in-k8s/issues/48)

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
--- PASS: TestOneNodeDaemonLaunchesPod (39.91s)
    --- PASS: TestOneNodeDaemonLaunchesPod/TestOneNodeDaemonLaunchesPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (20.77s)
    --- PASS: TestOneNodeDaemonLaunchesPod/TestOneNodeDaemonLaunchesPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (19.14s)
=== RUN   TestSimpleDaemonSetLaunchesPods
=== RUN   TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- FAIL: TestSimpleDaemonSetLaunchesPods (70.53s)
    --- PASS: TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (20.86s)
    --- FAIL: TestSimpleDaemonSetLaunchesPods/TestSimpleDaemonSetLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (49.67s)
=== RUN   TestDaemonSetWithNodeSelectorLaunchesPods
=== RUN   TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- PASS: TestDaemonSetWithNodeSelectorLaunchesPods (40.45s)
    --- PASS: TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (20.88s)
    --- PASS: TestDaemonSetWithNodeSelectorLaunchesPods/TestDaemonSetWithNodeSelectorLaunchesPods_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (19.57s)
=== RUN   TestNotReadyNodeDaemonDoesLaunchPod
=== RUN   TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
--- PASS: TestNotReadyNodeDaemonDoesLaunchPod (38.55s)
    --- PASS: TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,}) (19.38s)
    --- PASS: TestNotReadyNodeDaemonDoesLaunchPod/TestNotReadyNodeDaemonDoesLaunchPod_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},}) (19.16s)
=== RUN   TestInsufficientCapacityNode
=== RUN   TestInsufficientCapacityNode/TestInsufficientCapacityNode_(&DaemonSetUpdateStrategy{Type:OnDelete,RollingUpdate:nil,})
=== RUN   TestInsufficientCapacityNode/TestInsufficientCapacityNode_(&DaemonSetUpdateStrategy{Type:RollingUpdate,RollingUpdate:&RollingUpdateDaemonSet{MaxUnavailable:1,},})
=== RUN   TestAdmission
--- PASS: TestAdmission (3.95s)
=== RUN   TestNewDeployment
--- PASS: TestNewDeployment (15.53s)
=== RUN   TestDeploymentRollingUpdate
--- PASS: TestDeploymentRollingUpdate (58.47s)
=== RUN   TestDeploymentSelectorImmutability
--- PASS: TestDeploymentSelectorImmutability (5.01s)
=== RUN   TestPausedDeployment
--- PASS: TestPausedDeployment (9.38s)
=== RUN   TestScalePausedDeployment
--- PASS: TestScalePausedDeployment (9.90s)
=== RUN   TestDeploymentHashCollision
=== RUN   TestPDBWithScaleSubresource
--- FAIL: TestPDBWithScaleSubresource (33.74s)
=== RUN   TestDryRun//v1,_Resource=configmaps
=== RUN   TestDryRun//v1,_Resource=endpoints
=== RUN   TestDryRun//v1,_Resource=events
=== RUN   TestDryRun//v1,_Resource=limitranges
=== RUN   TestDryRun//v1,_Resource=namespaces
=== RUN   TestDryRun//v1,_Resource=nodes
=== RUN   TestDryRun//v1,_Resource=persistentvolumeclaims
=== RUN   TestDryRun//v1,_Resource=persistentvolumes
=== RUN   TestDryRun//v1,_Resource=pods
=== RUN   TestDryRun//v1,_Resource=podtemplates
=== RUN   TestDryRun//v1,_Resource=replicationcontrollers
=== RUN   TestDryRun//v1,_Resource=resourcequotas
=== RUN   TestDryRun//v1,_Resource=secrets
=== RUN   TestDryRun//v1,_Resource=serviceaccounts
=== RUN   TestDryRun//v1,_Resource=services
=== RUN   TestDryRun/apiregistration.k8s.io/v1,_Resource=apiservices
=== RUN   TestDryRun/apiregistration.k8s.io/v1beta1,_Resource=apiservices
=== RUN   TestDryRun/extensions/v1beta1,_Resource=ingresses
=== RUN   TestDryRun/apps/v1,_Resource=controllerrevisions
=== RUN   TestDryRun/apps/v1,_Resource=daemonsets
=== RUN   TestDryRun/apps/v1,_Resource=deployments
=== RUN   TestDryRun/apps/v1,_Resource=replicasets
=== RUN   TestDryRun/apps/v1,_Resource=statefulsets
=== RUN   TestDryRun/events.k8s.io/v1beta1,_Resource=events
=== RUN   TestDryRun/autoscaling/v1,_Resource=horizontalpodautoscalers
=== RUN   TestDryRun/autoscaling/v2beta1,_Resource=horizontalpodautoscalers
=== RUN   TestDryRun/autoscaling/v2beta2,_Resource=horizontalpodautoscalers
=== RUN   TestDryRun/batch/v1,_Resource=jobs
=== RUN   TestDryRun/batch/v1beta1,_Resource=cronjobs
=== RUN   TestDryRun/batch/v2alpha1,_Resource=cronjobs
=== RUN   TestDryRun/certificates.k8s.io/v1beta1,_Resource=certificatesigningrequests
=== RUN   TestDryRun/networking.k8s.io/v1,_Resource=networkpolicies
=== RUN   TestDryRun/networking.k8s.io/v1beta1,_Resource=ingressclasses
=== RUN   TestDryRun/networking.k8s.io/v1beta1,_Resource=ingresses
=== RUN   TestDryRun/policy/v1beta1,_Resource=poddisruptionbudgets
=== RUN   TestDryRun/policy/v1beta1,_Resource=podsecuritypolicies
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1,_Resource=clusterrolebindings
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1,_Resource=clusterroles
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1,_Resource=rolebindings
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1,_Resource=roles
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=clusterrolebindings
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=clusterroles
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=rolebindings
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=roles
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterrolebindings
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterroles
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=rolebindings
=== RUN   TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=roles
=== RUN   TestDryRun/settings.k8s.io/v1alpha1,_Resource=podpresets
=== RUN   TestDryRun/storage.k8s.io/v1,_Resource=csidrivers
=== RUN   TestDryRun/storage.k8s.io/v1,_Resource=csinodes
=== RUN   TestDryRun/storage.k8s.io/v1,_Resource=storageclasses
=== RUN   TestDryRun/storage.k8s.io/v1,_Resource=volumeattachments
=== RUN   TestDryRun/storage.k8s.io/v1beta1,_Resource=csidrivers
=== RUN   TestDryRun/storage.k8s.io/v1beta1,_Resource=csinodes
=== RUN   TestDryRun/storage.k8s.io/v1beta1,_Resource=storageclasses
=== RUN   TestDryRun/storage.k8s.io/v1beta1,_Resource=volumeattachments
=== RUN   TestDryRun/storage.k8s.io/v1alpha1,_Resource=volumeattachments
=== RUN   TestDryRun/admissionregistration.k8s.io/v1,_Resource=mutatingwebhookconfigurations
=== RUN   TestDryRun/admissionregistration.k8s.io/v1,_Resource=validatingwebhookconfigurations
=== RUN   TestDryRun/admissionregistration.k8s.io/v1beta1,_Resource=mutatingwebhookconfigurations
=== RUN   TestDryRun/admissionregistration.k8s.io/v1beta1,_Resource=validatingwebhookconfigurations
=== RUN   TestDryRun/apiextensions.k8s.io/v1,_Resource=customresourcedefinitions
=== RUN   TestDryRun/apiextensions.k8s.io/v1beta1,_Resource=customresourcedefinitions
=== RUN   TestDryRun/scheduling.k8s.io/v1,_Resource=priorityclasses
=== RUN   TestDryRun/scheduling.k8s.io/v1beta1,_Resource=priorityclasses
=== RUN   TestDryRun/scheduling.k8s.io/v1alpha1,_Resource=priorityclasses
=== RUN   TestDryRun/coordination.k8s.io/v1,_Resource=leases
=== RUN   TestDryRun/coordination.k8s.io/v1beta1,_Resource=leases
=== RUN   TestDryRun/auditregistration.k8s.io/v1alpha1,_Resource=auditsinks
=== RUN   TestDryRun/node.k8s.io/v1beta1,_Resource=runtimeclasses
=== RUN   TestDryRun/node.k8s.io/v1alpha1,_Resource=runtimeclasses
=== RUN   TestDryRun/discovery.k8s.io/v1beta1,_Resource=endpointslices
=== RUN   TestDryRun/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=flowschemas
=== RUN   TestDryRun/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=prioritylevelconfigurations
=== RUN   TestDryRun/awesome.bears.com/v3,_Resource=pandas
=== RUN   TestDryRun/awesome.bears.com/v1,_Resource=pandas
=== RUN   TestDryRun/cr.bar.com/v1,_Resource=foos
=== RUN   TestDryRun/random.numbers.com/v1,_Resource=integers
=== RUN   TestDryRun/custom.fancy.com/v2,_Resource=pants
--- PASS: TestDryRun (16.38s)
    --- PASS: TestDryRun//v1,_Resource=configmaps (0.10s)
    --- PASS: TestDryRun//v1,_Resource=endpoints (0.07s)
    --- PASS: TestDryRun//v1,_Resource=events (0.08s)
    --- PASS: TestDryRun//v1,_Resource=limitranges (0.09s)
    --- PASS: TestDryRun//v1,_Resource=namespaces (0.06s)
    --- PASS: TestDryRun//v1,_Resource=nodes (0.07s)
    --- PASS: TestDryRun//v1,_Resource=persistentvolumeclaims (0.59s)
    --- PASS: TestDryRun//v1,_Resource=persistentvolumes (0.07s)
    --- PASS: TestDryRun//v1,_Resource=pods (0.09s)
    --- PASS: TestDryRun//v1,_Resource=podtemplates (0.08s)
    --- PASS: TestDryRun//v1,_Resource=replicationcontrollers (0.10s)
    --- PASS: TestDryRun//v1,_Resource=resourcequotas (0.05s)
    --- PASS: TestDryRun//v1,_Resource=secrets (0.06s)
    --- PASS: TestDryRun//v1,_Resource=serviceaccounts (0.07s)
    --- PASS: TestDryRun//v1,_Resource=services (0.11s)
    --- PASS: TestDryRun/apiregistration.k8s.io/v1,_Resource=apiservices (0.34s)
    --- PASS: TestDryRun/apiregistration.k8s.io/v1beta1,_Resource=apiservices (0.19s)
    --- PASS: TestDryRun/extensions/v1beta1,_Resource=ingresses (0.10s)
    --- PASS: TestDryRun/apps/v1,_Resource=controllerrevisions (0.07s)
    --- PASS: TestDryRun/apps/v1,_Resource=daemonsets (0.06s)
    --- PASS: TestDryRun/apps/v1,_Resource=deployments (0.07s)
    --- PASS: TestDryRun/apps/v1,_Resource=replicasets (0.08s)
    --- PASS: TestDryRun/apps/v1,_Resource=statefulsets (0.10s)
    --- PASS: TestDryRun/events.k8s.io/v1beta1,_Resource=events (0.06s)
    --- PASS: TestDryRun/autoscaling/v1,_Resource=horizontalpodautoscalers (0.06s)
    --- PASS: TestDryRun/autoscaling/v2beta1,_Resource=horizontalpodautoscalers (0.06s)
    --- PASS: TestDryRun/autoscaling/v2beta2,_Resource=horizontalpodautoscalers (0.06s)
    --- PASS: TestDryRun/batch/v1,_Resource=jobs (0.05s)
    --- PASS: TestDryRun/batch/v1beta1,_Resource=cronjobs (0.05s)
    --- PASS: TestDryRun/batch/v2alpha1,_Resource=cronjobs (0.06s)
    --- PASS: TestDryRun/certificates.k8s.io/v1beta1,_Resource=certificatesigningrequests (0.06s)
    --- PASS: TestDryRun/networking.k8s.io/v1,_Resource=networkpolicies (0.05s)
    --- PASS: TestDryRun/networking.k8s.io/v1beta1,_Resource=ingressclasses (0.06s)
    --- PASS: TestDryRun/networking.k8s.io/v1beta1,_Resource=ingresses (0.05s)
    --- PASS: TestDryRun/policy/v1beta1,_Resource=poddisruptionbudgets (0.04s)
    --- PASS: TestDryRun/policy/v1beta1,_Resource=podsecuritypolicies (0.05s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1,_Resource=clusterrolebindings (0.05s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1,_Resource=clusterroles (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1,_Resource=rolebindings (0.05s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1,_Resource=roles (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=clusterrolebindings (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=clusterroles (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=rolebindings (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1beta1,_Resource=roles (0.06s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterrolebindings (0.05s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterroles (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=rolebindings (0.04s)
    --- PASS: TestDryRun/rbac.authorization.k8s.io/v1alpha1,_Resource=roles (0.05s)
    --- PASS: TestDryRun/settings.k8s.io/v1alpha1,_Resource=podpresets (0.05s)
    --- PASS: TestDryRun/storage.k8s.io/v1,_Resource=csidrivers (0.06s)
    --- PASS: TestDryRun/storage.k8s.io/v1,_Resource=csinodes (0.05s)
    --- PASS: TestDryRun/storage.k8s.io/v1,_Resource=storageclasses (0.04s)
    --- PASS: TestDryRun/storage.k8s.io/v1,_Resource=volumeattachments (0.04s)
    --- PASS: TestDryRun/storage.k8s.io/v1beta1,_Resource=csidrivers (0.09s)
    --- PASS: TestDryRun/storage.k8s.io/v1beta1,_Resource=csinodes (0.09s)
    --- PASS: TestDryRun/storage.k8s.io/v1beta1,_Resource=storageclasses (0.06s)
    --- PASS: TestDryRun/storage.k8s.io/v1beta1,_Resource=volumeattachments (0.05s)
    --- PASS: TestDryRun/storage.k8s.io/v1alpha1,_Resource=volumeattachments (0.05s)
    --- PASS: TestDryRun/admissionregistration.k8s.io/v1,_Resource=mutatingwebhookconfigurations (0.05s)
    --- PASS: TestDryRun/admissionregistration.k8s.io/v1,_Resource=validatingwebhookconfigurations (0.58s)
    --- PASS: TestDryRun/admissionregistration.k8s.io/v1beta1,_Resource=mutatingwebhookconfigurations (0.05s)
    --- PASS: TestDryRun/admissionregistration.k8s.io/v1beta1,_Resource=validatingwebhookconfigurations (0.05s)
    --- PASS: TestDryRun/apiextensions.k8s.io/v1,_Resource=customresourcedefinitions (0.08s)
    --- PASS: TestDryRun/apiextensions.k8s.io/v1beta1,_Resource=customresourcedefinitions (0.19s)
    --- PASS: TestDryRun/scheduling.k8s.io/v1,_Resource=priorityclasses (0.08s)
    --- PASS: TestDryRun/scheduling.k8s.io/v1beta1,_Resource=priorityclasses (0.08s)
    --- PASS: TestDryRun/scheduling.k8s.io/v1alpha1,_Resource=priorityclasses (0.05s)
    --- PASS: TestDryRun/coordination.k8s.io/v1,_Resource=leases (0.05s)
    --- PASS: TestDryRun/coordination.k8s.io/v1beta1,_Resource=leases (0.06s)
    --- PASS: TestDryRun/auditregistration.k8s.io/v1alpha1,_Resource=auditsinks (0.05s)
    --- PASS: TestDryRun/node.k8s.io/v1beta1,_Resource=runtimeclasses (0.05s)
    --- PASS: TestDryRun/node.k8s.io/v1alpha1,_Resource=runtimeclasses (0.04s)
    --- PASS: TestDryRun/discovery.k8s.io/v1beta1,_Resource=endpointslices (0.06s)
    --- PASS: TestDryRun/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=flowschemas (0.08s)
    --- PASS: TestDryRun/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=prioritylevelconfigurations (0.05s)
    --- PASS: TestDryRun/awesome.bears.com/v3,_Resource=pandas (0.15s)
    --- PASS: TestDryRun/awesome.bears.com/v1,_Resource=pandas (0.06s)
    --- PASS: TestDryRun/cr.bar.com/v1,_Resource=foos (0.09s)
    --- PASS: TestDryRun/random.numbers.com/v1,_Resource=integers (0.17s)
    --- PASS: TestDryRun/custom.fancy.com/v2,_Resource=pants (0.09s)
=== RUN   TestOverlappingBuiltInResources
--- PASS: TestOverlappingBuiltInResources (0.00s)
=== RUN   TestOverlappingCustomResourceAPIService
--- PASS: TestOverlappingCustomResourceAPIService (18.01s)
=== RUN   TestOverlappingCustomResourceCustomResourceDefinition
--- PASS: TestOverlappingCustomResourceCustomResourceDefinition (16.75s)
=== RUN   TestCrossGroupStorage
=== RUN   TestCrossGroupStorage//v1,_Kind=Event
=== RUN   TestCrossGroupStorage/networking.k8s.io/v1beta1,_Kind=Ingress
--- FAIL: TestCrossGroupStorage (71.79s)
    --- PASS: TestCrossGroupStorage/networking.k8s.io/v1beta1,_Kind=Ingress (0.05s)
    --- FAIL: TestCrossGroupStorage//v1,_Kind=Event (60.06s)
=== RUN   TestEtcdStoragePath
=== RUN   TestEtcdStoragePath//v1,_Resource=configmaps
=== RUN   TestEtcdStoragePath//v1,_Resource=endpoints
=== RUN   TestEtcdStoragePath//v1,_Resource=events
=== RUN   TestEtcdStoragePath//v1,_Resource=limitranges
=== RUN   TestEtcdStoragePath//v1,_Resource=namespaces
=== RUN   TestEtcdStoragePath//v1,_Resource=nodes
=== RUN   TestEtcdStoragePath//v1,_Resource=persistentvolumeclaims
=== RUN   TestEtcdStoragePath//v1,_Resource=persistentvolumes
=== RUN   TestEtcdStoragePath//v1,_Resource=pods
=== RUN   TestEtcdStoragePath//v1,_Resource=podtemplates
=== RUN   TestEtcdStoragePath//v1,_Resource=replicationcontrollers
=== RUN   TestEtcdStoragePath//v1,_Resource=resourcequotas
=== RUN   TestEtcdStoragePath//v1,_Resource=secrets
=== RUN   TestEtcdStoragePath//v1,_Resource=serviceaccounts
=== RUN   TestEtcdStoragePath//v1,_Resource=services
=== RUN   TestEtcdStoragePath/apiregistration.k8s.io/v1,_Resource=apiservices
=== RUN   TestEtcdStoragePath/apiregistration.k8s.io/v1beta1,_Resource=apiservices
=== RUN   TestEtcdStoragePath/extensions/v1beta1,_Resource=ingresses
=== RUN   TestEtcdStoragePath/apps/v1,_Resource=controllerrevisions
=== RUN   TestEtcdStoragePath/apps/v1,_Resource=daemonsets
=== RUN   TestEtcdStoragePath/apps/v1,_Resource=deployments
=== RUN   TestEtcdStoragePath/apps/v1,_Resource=replicasets
=== RUN   TestEtcdStoragePath/apps/v1,_Resource=statefulsets
=== RUN   TestEtcdStoragePath/events.k8s.io/v1beta1,_Resource=events
=== RUN   TestEtcdStoragePath/autoscaling/v1,_Resource=horizontalpodautoscalers
=== RUN   TestEtcdStoragePath/autoscaling/v2beta1,_Resource=horizontalpodautoscalers
=== RUN   TestEtcdStoragePath/autoscaling/v2beta2,_Resource=horizontalpodautoscalers
=== RUN   TestEtcdStoragePath/batch/v1,_Resource=jobs
=== RUN   TestEtcdStoragePath/batch/v1beta1,_Resource=cronjobs
=== RUN   TestEtcdStoragePath/batch/v2alpha1,_Resource=cronjobs
=== RUN   TestEtcdStoragePath/certificates.k8s.io/v1beta1,_Resource=certificatesigningrequests
=== RUN   TestEtcdStoragePath/networking.k8s.io/v1,_Resource=networkpolicies
=== RUN   TestEtcdStoragePath/networking.k8s.io/v1beta1,_Resource=ingressclasses
=== RUN   TestEtcdStoragePath/networking.k8s.io/v1beta1,_Resource=ingresses
=== RUN   TestEtcdStoragePath/policy/v1beta1,_Resource=poddisruptionbudgets
=== RUN   TestEtcdStoragePath/policy/v1beta1,_Resource=podsecuritypolicies
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=clusterrolebindings
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=clusterroles
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=rolebindings
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=roles
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=clusterrolebindings
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=clusterroles
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=rolebindings
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=roles
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterrolebindings
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterroles
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=rolebindings
=== RUN   TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=roles
=== RUN   TestEtcdStoragePath/settings.k8s.io/v1alpha1,_Resource=podpresets
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1,_Resource=csidrivers
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1,_Resource=csinodes
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1,_Resource=storageclasses
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1,_Resource=volumeattachments
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=csidrivers
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=csinodes
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=storageclasses
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=volumeattachments
=== RUN   TestEtcdStoragePath/storage.k8s.io/v1alpha1,_Resource=volumeattachments
=== RUN   TestEtcdStoragePath/admissionregistration.k8s.io/v1,_Resource=mutatingwebhookconfigurations
=== RUN   TestEtcdStoragePath/admissionregistration.k8s.io/v1,_Resource=validatingwebhookconfigurations
=== RUN   TestEtcdStoragePath/admissionregistration.k8s.io/v1beta1,_Resource=mutatingwebhookconfigurations
=== RUN   TestEtcdStoragePath/admissionregistration.k8s.io/v1beta1,_Resource=validatingwebhookconfigurations
=== RUN   TestEtcdStoragePath/apiextensions.k8s.io/v1,_Resource=customresourcedefinitions
=== RUN   TestEtcdStoragePath/apiextensions.k8s.io/v1beta1,_Resource=customresourcedefinitions
=== RUN   TestEtcdStoragePath/scheduling.k8s.io/v1,_Resource=priorityclasses
=== RUN   TestEtcdStoragePath/scheduling.k8s.io/v1beta1,_Resource=priorityclasses
=== RUN   TestEtcdStoragePath/scheduling.k8s.io/v1alpha1,_Resource=priorityclasses
=== RUN   TestEtcdStoragePath/coordination.k8s.io/v1,_Resource=leases
=== RUN   TestEtcdStoragePath/coordination.k8s.io/v1beta1,_Resource=leases
=== RUN   TestEtcdStoragePath/auditregistration.k8s.io/v1alpha1,_Resource=auditsinks
=== RUN   TestEtcdStoragePath/node.k8s.io/v1beta1,_Resource=runtimeclasses
=== RUN   TestEtcdStoragePath/node.k8s.io/v1alpha1,_Resource=runtimeclasses
=== RUN   TestEtcdStoragePath/discovery.k8s.io/v1beta1,_Resource=endpointslices
=== RUN   TestEtcdStoragePath/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=flowschemas
=== RUN   TestEtcdStoragePath/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=prioritylevelconfigurations
=== RUN   TestEtcdStoragePath/awesome.bears.com/v3,_Resource=pandas
=== RUN   TestEtcdStoragePath/awesome.bears.com/v1,_Resource=pandas
=== RUN   TestEtcdStoragePath/cr.bar.com/v1,_Resource=foos
=== RUN   TestEtcdStoragePath/random.numbers.com/v1,_Resource=integers
=== RUN   TestEtcdStoragePath/custom.fancy.com/v2,_Resource=pants
--- PASS: TestEtcdStoragePath (14.40s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=configmaps (0.03s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=endpoints (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=events (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=limitranges (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=namespaces (0.01s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=nodes (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=persistentvolumeclaims (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=persistentvolumes (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=pods (0.03s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=podtemplates (0.03s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=replicationcontrollers (0.03s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=resourcequotas (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=secrets (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=serviceaccounts (0.02s)
    --- PASS: TestEtcdStoragePath//v1,_Resource=services (0.04s)
    --- PASS: TestEtcdStoragePath/apiregistration.k8s.io/v1,_Resource=apiservices (0.02s)
    --- PASS: TestEtcdStoragePath/apiregistration.k8s.io/v1beta1,_Resource=apiservices (0.02s)
    --- PASS: TestEtcdStoragePath/extensions/v1beta1,_Resource=ingresses (0.02s)
    --- PASS: TestEtcdStoragePath/apps/v1,_Resource=controllerrevisions (0.02s)
    --- PASS: TestEtcdStoragePath/apps/v1,_Resource=daemonsets (0.03s)
    --- PASS: TestEtcdStoragePath/apps/v1,_Resource=deployments (0.02s)
    --- PASS: TestEtcdStoragePath/apps/v1,_Resource=replicasets (0.02s)
    --- PASS: TestEtcdStoragePath/apps/v1,_Resource=statefulsets (0.03s)
    --- PASS: TestEtcdStoragePath/events.k8s.io/v1beta1,_Resource=events (0.03s)
    --- PASS: TestEtcdStoragePath/autoscaling/v1,_Resource=horizontalpodautoscalers (0.04s)
    --- PASS: TestEtcdStoragePath/autoscaling/v2beta1,_Resource=horizontalpodautoscalers (0.02s)
    --- PASS: TestEtcdStoragePath/autoscaling/v2beta2,_Resource=horizontalpodautoscalers (0.03s)
    --- PASS: TestEtcdStoragePath/batch/v1,_Resource=jobs (0.04s)
    --- PASS: TestEtcdStoragePath/batch/v1beta1,_Resource=cronjobs (0.07s)
    --- PASS: TestEtcdStoragePath/batch/v2alpha1,_Resource=cronjobs (0.03s)
    --- PASS: TestEtcdStoragePath/certificates.k8s.io/v1beta1,_Resource=certificatesigningrequests (0.02s)
    --- PASS: TestEtcdStoragePath/networking.k8s.io/v1,_Resource=networkpolicies (0.02s)
    --- PASS: TestEtcdStoragePath/networking.k8s.io/v1beta1,_Resource=ingressclasses (0.02s)
    --- PASS: TestEtcdStoragePath/networking.k8s.io/v1beta1,_Resource=ingresses (0.02s)
    --- PASS: TestEtcdStoragePath/policy/v1beta1,_Resource=poddisruptionbudgets (0.02s)
    --- PASS: TestEtcdStoragePath/policy/v1beta1,_Resource=podsecuritypolicies (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=clusterrolebindings (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=clusterroles (0.01s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=rolebindings (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1,_Resource=roles (0.01s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=clusterrolebindings (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=clusterroles (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=rolebindings (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1beta1,_Resource=roles (0.03s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterrolebindings (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=clusterroles (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=rolebindings (0.02s)
    --- PASS: TestEtcdStoragePath/rbac.authorization.k8s.io/v1alpha1,_Resource=roles (0.03s)
    --- PASS: TestEtcdStoragePath/settings.k8s.io/v1alpha1,_Resource=podpresets (0.04s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1,_Resource=csidrivers (0.04s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1,_Resource=csinodes (0.03s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1,_Resource=storageclasses (0.03s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1,_Resource=volumeattachments (0.02s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=csidrivers (0.02s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=csinodes (0.02s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=storageclasses (0.02s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1beta1,_Resource=volumeattachments (0.03s)
    --- PASS: TestEtcdStoragePath/storage.k8s.io/v1alpha1,_Resource=volumeattachments (0.02s)
    --- PASS: TestEtcdStoragePath/admissionregistration.k8s.io/v1,_Resource=mutatingwebhookconfigurations (0.02s)
    --- PASS: TestEtcdStoragePath/admissionregistration.k8s.io/v1,_Resource=validatingwebhookconfigurations (0.02s)
    --- PASS: TestEtcdStoragePath/admissionregistration.k8s.io/v1beta1,_Resource=mutatingwebhookconfigurations (0.01s)
    --- PASS: TestEtcdStoragePath/admissionregistration.k8s.io/v1beta1,_Resource=validatingwebhookconfigurations (0.02s)
    --- PASS: TestEtcdStoragePath/apiextensions.k8s.io/v1,_Resource=customresourcedefinitions (0.03s)
    --- PASS: TestEtcdStoragePath/apiextensions.k8s.io/v1beta1,_Resource=customresourcedefinitions (0.09s)
    --- PASS: TestEtcdStoragePath/scheduling.k8s.io/v1,_Resource=priorityclasses (0.05s)
    --- PASS: TestEtcdStoragePath/scheduling.k8s.io/v1beta1,_Resource=priorityclasses (0.03s)
    --- PASS: TestEtcdStoragePath/scheduling.k8s.io/v1alpha1,_Resource=priorityclasses (0.05s)
    --- PASS: TestEtcdStoragePath/coordination.k8s.io/v1,_Resource=leases (0.04s)
    --- PASS: TestEtcdStoragePath/coordination.k8s.io/v1beta1,_Resource=leases (0.03s)
    --- PASS: TestEtcdStoragePath/auditregistration.k8s.io/v1alpha1,_Resource=auditsinks (0.04s)
    --- PASS: TestEtcdStoragePath/node.k8s.io/v1beta1,_Resource=runtimeclasses (0.02s)
    --- PASS: TestEtcdStoragePath/node.k8s.io/v1alpha1,_Resource=runtimeclasses (0.03s)
    --- PASS: TestEtcdStoragePath/discovery.k8s.io/v1beta1,_Resource=endpointslices (0.03s)
    --- PASS: TestEtcdStoragePath/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=flowschemas (0.02s)
    --- PASS: TestEtcdStoragePath/flowcontrol.apiserver.k8s.io/v1alpha1,_Resource=prioritylevelconfigurations (0.02s)
    --- PASS: TestEtcdStoragePath/awesome.bears.com/v3,_Resource=pandas (0.10s)
    --- PASS: TestEtcdStoragePath/awesome.bears.com/v1,_Resource=pandas (0.03s)
    --- PASS: TestEtcdStoragePath/cr.bar.com/v1,_Resource=foos (0.07s)
    --- PASS: TestEtcdStoragePath/random.numbers.com/v1,_Resource=integers (0.06s)
    --- PASS: TestEtcdStoragePath/custom.fancy.com/v2,_Resource=pants (0.07s)
=== RUN   TestConcurrentEvictionRequests
--- PASS: TestConcurrentEvictionRequests (11.11s)
=== RUN   TestTerminalPodEviction
--- PASS: TestTerminalPodEviction (6.16s)
=== RUN   TestAggregatedAPIServer
--- FAIL: TestAggregatedAPIServer (31.92s)
=== RUN   TestWebhookLoopback
--- PASS: TestWebhookLoopback (6.04s)
=== RUN   TestClusterScopedOwners
--- FAIL: TestClusterScopedOwners (32.57s)
=== RUN   TestCascadingDeletion
--- PASS: TestCascadingDeletion (8.70s)
=== RUN   TestCreateWithNonExistentOwner
--- PASS: TestCreateWithNonExistentOwner (8.72s)
=== RUN   TestStressingCascadingDeletion
--- FAIL: TestStressingCascadingDeletion (32.23s)
=== RUN   TestOrphaning
--- FAIL: TestOrphaning (33.94s)
=== RUN   TestSolidOwnerDoesNotBlockWaitingOwner
--- PASS: TestSolidOwnerDoesNotBlockWaitingOwner (8.31s)
=== RUN   TestNonBlockingOwnerRefDoesNotBlock
--- PASS: TestNonBlockingOwnerRefDoesNotBlock (8.56s)
=== RUN   TestDoubleDeletionWithFinalizer
--- PASS: TestDoubleDeletionWithFinalizer (11.85s)
=== RUN   TestBlockingOwnerRefDoesBlock
--- PASS: TestBlockingOwnerRefDoesBlock (23.24s)
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
=== RUN   TestDynamicAudit/two_sink
=== RUN   TestDynamicAudit/delete_sink
=== RUN   TestDynamicAudit/update_sink
--- PASS: TestDynamicAudit (17.47s)
    --- PASS: TestDynamicAudit/one_sink (1.06s)
    --- PASS: TestDynamicAudit/two_sink (1.05s)
    --- PASS: TestDynamicAudit/delete_sink (1.06s)
    --- PASS: TestDynamicAudit/update_sink (6.65s)
=== RUN   TestAudit
=== RUN   TestAudit/audit.k8s.io/v1.RequestResponse.false
=== RUN   TestAudit/audit.k8s.io/v1.Metadata.true
=== RUN   TestAudit/audit.k8s.io/v1.Request.true
=== RUN   TestAudit/audit.k8s.io/v1.RequestResponse.true
=== RUN   TestAudit/audit.k8s.io/v1beta1.RequestResponse.false
=== RUN   TestAudit/audit.k8s.io/v1beta1.Metadata.true
=== RUN   TestAudit/audit.k8s.io/v1beta1.Request.true
=== RUN   TestAudit/audit.k8s.io/v1beta1.RequestResponse.true
--- FAIL: TestAudit (143.17s)
    --- FAIL: TestAudit/audit.k8s.io/v1.RequestResponse.false (32.65s)
    --- FAIL: TestAudit/audit.k8s.io/v1.Metadata.true (31.58s)
    --- PASS: TestAudit/audit.k8s.io/v1.Request.true (9.51s)
    --- FAIL: TestAudit/audit.k8s.io/v1.RequestResponse.true (32.39s)
    --- PASS: TestAudit/audit.k8s.io/v1beta1.RequestResponse.false (9.38s)
    --- PASS: TestAudit/audit.k8s.io/v1beta1.Metadata.true (9.19s)
    --- PASS: TestAudit/audit.k8s.io/v1beta1.Request.true (9.41s)
    --- PASS: TestAudit/audit.k8s.io/v1beta1.RequestResponse.true (9.05s)
=== RUN   TestCRDShadowGroup
--- PASS: TestCRDShadowGroup (10.20s)
=== RUN   TestCRD
--- PASS: TestCRD (8.22s)
=== RUN   TestCRDOpenAPI
--- PASS: TestCRDOpenAPI (23.39s)
=== RUN   TestGracefulShutdown
--- PASS: TestGracefulShutdown (8.71s)
=== RUN   TestKMSProvider
--- PASS: TestKMSProvider (7.85s)
=== RUN   TestKMSHealthz
--- PASS: TestKMSHealthz (48.57s)
=== RUN   TestRun
--- PASS: TestRun (7.67s)
=== RUN   TestLivezAndReadyz
--- FAIL: TestLivezAndReadyz (31.74s)
=== RUN   TestOpenAPIDelegationChainPlumbing
--- PASS: TestOpenAPIDelegationChainPlumbing (7.45s)
=== RUN   TestOpenAPIApiextensionsOverlapProtection
--- PASS: TestOpenAPIApiextensionsOverlapProtection (31.35s)
=== RUN   TestReconcilerMasterLeaseCombined
--- PASS: TestReconcilerMasterLeaseCombined (40.95s)
=== RUN   TestReconcilerMasterLeaseMultiMoreMasters
--- PASS: TestReconcilerMasterLeaseMultiMoreMasters (41.85s)
=== RUN   TestReconcilerMasterLeaseMultiCombined
--- FAIL: TestReconcilerMasterLeaseMultiCombined (31.51s)
=== RUN   TestSecretsShouldBeTransformed
--- FAIL: TestSecretsShouldBeTransformed (38.92s)
=== RUN   TestMasterProcessMetrics
--- PASS: TestMasterProcessMetrics (6.09s)
=== RUN   TestApiserverMetrics
--- PASS: TestApiserverMetrics (4.03s)
=== RUN   TestNamespaceCondition
--- PASS: TestNamespaceCondition (11.66s)
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_200_tolerationseconds
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_with_no_pod_tolerations
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_0_tolerationseconds
=== RUN   TestTaintBasedEvictions/Taint_based_evictions_for_NodeUnreachable
--- PASS: TestTaintBasedEvictions (43.91s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_200_tolerationseconds (12.25s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_with_no_pod_tolerations (10.64s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeNotReady_and_0_tolerationseconds (10.60s)
    --- PASS: TestTaintBasedEvictions/Taint_based_evictions_for_NodeUnreachable (10.43s)
=== RUN   TestIgnoreClusterName
--- PASS: TestIgnoreClusterName (5.97s)
=== RUN   TestMasterExportsSymbols
--- PASS: TestMasterExportsSymbols (0.00s)
=== RUN   TestPodUpdateActiveDeadlineSeconds
--- PASS: TestPodUpdateActiveDeadlineSeconds (10.00s)
=== RUN   TestPodReadOnlyFilesystem
--- PASS: TestPodReadOnlyFilesystem (4.37s)
=== RUN   TestPodCreateEphemeralContainers
--- PASS: TestPodCreateEphemeralContainers (4.07s)
=== RUN   TestPodPatchEphemeralContainers
--- PASS: TestPodPatchEphemeralContainers (9.72s)
=== RUN   TestPodUpdateEphemeralContainers
--- PASS: TestPodUpdateEphemeralContainers (11.11s)
=== RUN   TestQuota
--- FAIL: TestQuota (186.08s)
=== RUN   TestQuotaLimitedResourceDenial
--- PASS: TestQuotaLimitedResourceDenial (4.35s)
=== RUN   TestAdoption
--- PASS: TestAdoption (18.14s)
=== RUN   TestRSSelectorImmutability
--- PASS: TestRSSelectorImmutability (4.11s)
=== RUN   TestSpecReplicasChange
--- PASS: TestSpecReplicasChange (6.04s)
=== RUN   TestDeletingAndFailedPods
--- PASS: TestDeletingAndFailedPods (4.43s)
=== RUN   TestOverlappingRSs
--- PASS: TestOverlappingRSs (4.47s)
=== RUN   TestPodOrphaningAndAdoptionWhenLabelsChange
--- PASS: TestPodOrphaningAndAdoptionWhenLabelsChange (4.49s)
=== RUN   TestGeneralPodAdoption
--- PASS: TestGeneralPodAdoption (5.01s)
=== RUN   TestReadyAndAvailableReplicas
--- PASS: TestReadyAndAvailableReplicas (4.29s)
=== RUN   TestRSScaleSubresource
--- PASS: TestRSScaleSubresource (4.78s)
=== RUN   TestExtraPodsAdoptionAndDeletion
--- PASS: TestExtraPodsAdoptionAndDeletion (4.28s)
=== RUN   TestFullyLabeledReplicas
--- PASS: TestFullyLabeledReplicas (4.17s)
=== RUN   TestReplicaSetsAppsV1DefaultGCPolicy
--- PASS: TestReplicaSetsAppsV1DefaultGCPolicy (4.40s)
=== RUN   TestAdoption
--- PASS: TestAdoption (18.99s)
=== RUN   TestSpecReplicasChange
--- PASS: TestSpecReplicasChange (8.11s)
=== RUN   TestDeletingAndFailedPods
--- PASS: TestDeletingAndFailedPods (4.20s)
=== RUN   TestOverlappingRCs
--- PASS: TestOverlappingRCs (4.30s)
=== RUN   TestPodOrphaningAndAdoptionWhenLabelsChange
--- PASS: TestPodOrphaningAndAdoptionWhenLabelsChange (4.36s)
=== RUN   TestGeneralPodAdoption
--- PASS: TestGeneralPodAdoption (4.77s)
=== RUN   TestReadyAndAvailableReplicas
--- PASS: TestReadyAndAvailableReplicas (4.47s)
=== RUN   TestRCScaleSubresource
--- PASS: TestRCScaleSubresource (5.69s)
=== RUN   TestExtraPodsAdoptionAndDeletion
--- PASS: TestExtraPodsAdoptionAndDeletion (4.78s)
=== RUN   TestFullyLabeledReplicas
--- PASS: TestFullyLabeledReplicas (4.43s)
=== RUN   TestAdoption
--- PASS: TestAdoption (18.99s)
=== RUN   TestSpecReplicasChange
--- PASS: TestSpecReplicasChange (8.11s)
=== RUN   TestDeletingAndFailedPods
--- PASS: TestDeletingAndFailedPods (4.20s)
=== RUN   TestOverlappingRCs
--- PASS: TestOverlappingRCs (4.30s)
=== RUN   TestPodOrphaningAndAdoptionWhenLabelsChange
--- PASS: TestPodOrphaningAndAdoptionWhenLabelsChange (4.36s)
=== RUN   TestGeneralPodAdoption
--- PASS: TestGeneralPodAdoption (4.77s)
=== RUN   TestReadyAndAvailableReplicas
--- PASS: TestReadyAndAvailableReplicas (4.47s)
=== RUN   TestRCScaleSubresource
--- PASS: TestRCScaleSubresource (5.69s)
=== RUN   TestExtraPodsAdoptionAndDeletion
--- PASS: TestExtraPodsAdoptionAndDeletion (4.78s)
=== RUN   TestFullyLabeledReplicas
--- PASS: TestFullyLabeledReplicas (4.43s)
=== RUN   TestScaleSubresources
--- FAIL: TestScaleSubresources (32.56s)

=== RUN   TestSchedulerExtender
--- PASS: TestSchedulerExtender (6.96s)
=== RUN   TestPreFilterPlugin
--- PASS: TestPreFilterPlugin (4.68s)
=== RUN   TestScorePlugin
--- PASS: TestScorePlugin (4.50s)
=== RUN   TestNormalizeScorePlugin
--- PASS: TestNormalizeScorePlugin (4.31s)
=== RUN   TestReservePlugin
--- PASS: TestReservePlugin (4.25s)
=== RUN   TestPrebindPlugin
--- PASS: TestPrebindPlugin (4.37s)
=== RUN   TestUnreservePlugin
--- PASS: TestUnreservePlugin (4.33s)
=== RUN   TestBindPlugin
--- PASS: TestBindPlugin (5.93s)
=== RUN   TestPostBindPlugin
--- PASS: TestPostBindPlugin (4.43s)
=== RUN   TestPermitPlugin
--- PASS: TestPermitPlugin (10.72s)
=== RUN   TestMultiplePermitPlugins
--- PASS: TestMultiplePermitPlugins (4.22s)
=== RUN   TestPermitPluginsCancelled
--- PASS: TestPermitPluginsCancelled (4.28s)
=== RUN   TestCoSchedulingWithPermitPlugin
--- PASS: TestCoSchedulingWithPermitPlugin (4.79s)
=== RUN   TestFilterPlugin
--- PASS: TestFilterPlugin (4.36s)
=== RUN   TestPreScorePlugin
--- PASS: TestPreScorePlugin (4.84s)
=== RUN   TestPreemptWithPermitPlugin
--- PASS: TestPreemptWithPermitPlugin (4.36s)
=== RUN   TestNodeResourceLimits
--- PASS: TestNodeResourceLimits (4.36s)
=== RUN   TestInterPodAffinity
--- PASS: TestInterPodAffinity (12.45s)
=== RUN   TestEvenPodsSpreadPredicate
=== RUN   TestEvenPodsSpreadPredicate/place_pod_on_a_1/1/0/1_cluster_with_MaxSkew=1,_node-2_is_the_only_fit
=== RUN   TestEvenPodsSpreadPredicate/place_pod_on_a_2/0/0/1_cluster_with_MaxSkew=2,_node-{1,2,3}_are_good_fits
=== RUN   TestEvenPodsSpreadPredicate/pod_is_required_to_be_placed_on_zone0,_so_only_node-1_fits
=== RUN   TestEvenPodsSpreadPredicate/two_constraints:_pod_can_only_be_placed_to_zone-1/node-2
=== RUN   TestEvenPodsSpreadPredicate/pod_cannot_be_placed_onto_any_node
=== RUN   TestEvenPodsSpreadPredicate/high_priority_pod_can_preempt_others
    --- PASS: TestEvenPodsSpreadPredicate/place_pod_on_a_1/1/0/1_cluster_with_MaxSkew=1,_node-2_is_the_only_fit (0.57s)
    --- PASS: TestEvenPodsSpreadPredicate/place_pod_on_a_2/0/0/1_cluster_with_MaxSkew=2,_node-{1,2,3}_are_good_fits (0.55s)
    --- PASS: TestEvenPodsSpreadPredicate/pod_is_required_to_be_placed_on_zone0,_so_only_node-1_fits (0.41s)
    --- PASS: TestEvenPodsSpreadPredicate/two_constraints:_pod_can_only_be_placed_to_zone-1/node-2 (0.71s)
    --- PASS: TestEvenPodsSpreadPredicate/pod_cannot_be_placed_onto_any_node (0.83s)
    --- PASS: TestEvenPodsSpreadPredicate/high_priority_pod_can_preempt_others (0.85s)
=== RUN   TestPreemption
--- PASS: TestPreemption (11.15s)
=== RUN   TestDisablePreemption
--- PASS: TestDisablePreemption (11.09s)
=== RUN   TestPodPriorityResolution
=== RUN   TestPodPriorityResolution/SystemNodeCritical_priority_class
=== RUN   TestPodPriorityResolution/SystemClusterCritical_priority_class
=== RUN   TestPodPriorityResolution/Invalid_priority_class_should_result_in_error
--- PASS: TestPodPriorityResolution (5.75s)
    --- PASS: TestPodPriorityResolution/SystemNodeCritical_priority_class (0.14s)
    --- PASS: TestPodPriorityResolution/SystemClusterCritical_priority_class (0.13s)
    --- PASS: TestPodPriorityResolution/Invalid_priority_class_should_result_in_error (0.00s)
=== RUN   TestPreemptionStarvation
--- PASS: TestPreemptionStarvation (6.54s)
=== RUN   TestPreemptionRaces
--- FAIL: TestPreemptionRaces (34.34s)
=== RUN   TestNominatedNodeCleanUp
--- FAIL: TestNominatedNodeCleanUp (35.22s)
=== RUN   TestPDBInPreemption
--- PASS: TestPDBInPreemption (18.48s)
=== RUN   TestNodeAffinity
--- PASS: TestNodeAffinity (5.76s)
=== RUN   TestPodAffinity
--- PASS: TestPodAffinity (5.30s)
=== RUN   TestImageLocality
--- PASS: TestImageLocality (4.47s)
--- PASS: TestEvenPodsSpreadPriority (8.49s)
    --- PASS: TestEvenPodsSpreadPriority/place_pod_on_a_~0~/1/2/3_cluster_with_MaxSkew=1,_node-1_is_the_preferred_fit (1.01s)
    --- PASS: TestEvenPodsSpreadPriority/combined_with_hardSpread_constraint_on_a_~4~/0/1/2_cluster (1.12s)
=== RUN   TestSchedulerCreationFromConfigMap
--- PASS: TestSchedulerCreationFromConfigMap (6.19s)
=== RUN   TestSchedulerCreationFromNonExistentConfigMap
--- PASS: TestSchedulerCreationFromNonExistentConfigMap (4.05s)
=== RUN   TestSchedule100Node3KPods
--- SKIP: TestSchedule100Node3KPods (0.00s)
=== RUN   TestSecrets
--- PASS: TestSecrets (5.26s)
=== RUN   TestServiceAccountAutoCreate
--- PASS: TestServiceAccountAutoCreate (8.81s)
=== RUN   TestServiceAccountTokenAutoCreate
--- PASS: TestServiceAccountTokenAutoCreate (13.16s)
=== RUN   TestServiceAccountTokenAutoMount
--- FAIL: TestServiceAccountTokenAutoMount (14.73s)
=== RUN   TestServiceAccountTokenAuthentication
--- PASS: TestServiceAccountTokenAuthentication (40.24s)
=== RUN   TestComponentSecureServingAndAuth
--- FAIL: TestComponentSecureServingAndAuth (32.64s)
=== RUN   TestVolumeTemplateNoopUpdate
--- FAIL: TestVolumeTemplateNoopUpdate (32.28s)
=== RUN   TestSpecReplicasChange
--- PASS: TestSpecReplicasChange (6.88s)
=== RUN   TestDeletingAndFailedPods
--- PASS: TestDeletingAndFailedPods (4.90s)
=== RUN   TestStorageClasses
--- PASS: TestStorageClasses (5.13s)
=== RUN   TestAPICiphers
--- PASS: TestAPICiphers (8.96s)
=== RUN   TestTTLAnnotations
--- FAIL: TestTTLAnnotations (35.56s)
=== RUN   TestPodDeletionWithDswp
--- PASS: TestPodDeletionWithDswp (6.78s)
=== RUN   TestPodUpdateWithWithADC
--- PASS: TestPodUpdateWithWithADC (5.41s)
=== RUN   TestPodUpdateWithKeepTerminatedPodVolumes
--- PASS: TestPodUpdateWithKeepTerminatedPodVolumes (5.11s)
=== RUN   TestPodAddedByDswp
--- PASS: TestPodAddedByDswp (7.16s)
=== RUN   TestPVCBoundWithADC
--- PASS: TestPVCBoundWithADC (19.82s)
=== RUN   TestPersistentVolumeRecycler
--- PASS: TestPersistentVolumeRecycler (5.16s)
=== RUN   TestPersistentVolumeDeleter
--- PASS: TestPersistentVolumeDeleter (5.14s)
=== RUN   TestPersistentVolumeBindRace
--- PASS: TestPersistentVolumeBindRace (6.22s)
=== RUN   TestPersistentVolumeClaimLabelSelector
--- PASS: TestPersistentVolumeClaimLabelSelector (5.07s)
=== RUN   TestPersistentVolumeClaimLabelSelectorMatchExpressions
--- PASS: TestPersistentVolumeClaimLabelSelectorMatchExpressions (5.42s)
=== RUN   TestPersistentVolumeMultiPVs
--- PASS: TestPersistentVolumeMultiPVs (7.47s)
=== RUN   TestPersistentVolumeMultiPVsPVCs
panic: test timed out after 10m0s
=== RUN   TestVolumeBinding
--- FAIL: TestVolumeBinding (43.39s)
=== RUN   TestVolumeBindingRescheduling
--- PASS: TestVolumeBindingRescheduling (17.42s)
=== RUN   TestVolumeBindingStress
--- FAIL: TestVolumeBindingStress (488.10s)
=== RUN   TestVolumeBindingStressWithSchedulerResync
panic: test timed out after 10m0s
```