# List of Integration Test Failures
The following tests failed during the integration testing run:
* `apiserver/TestListResourceVersion0/watchCacheOff`
* `apiserver/TestAPIListChunking`
* `apiserver/TestMetadataClient`
* `apiserver/TestAPICRDProtobuf`
* `apiserver/TestTransform`
* `auth`
* `certificates`
* `master/TestDynamicAudit`

Full test logs are located here: [output](output)

## `apiserver/TestAPIListChunking`
The test fails with the error below. Most likely, this is related to pagination which is not implemented in `ignite-etcd` yet. We expect the test to pass once the functionality is added (planned for later phases of the project).

```
apiserver_test.go:414: unexpected list invocations: 1
apiserver_test.go:432: unexpected items: &v1.ReplicaSetList{TypeMeta:v1.TypeMeta{Kind:"", APIVersion:""}, ListMeta:v1.ListMeta{SelfLink:"/apis/apps/v1/namespaces/list-paging/replicasets", ResourceVersion:"14564", Continue:"", RemainingItemCount:(*int64)(nil)}, Items:[]v1.ReplicaSet{v1.ReplicaSet{TypeMeta:v1.TypeMeta{Kind:"", APIVersion:""}, ObjectMeta:v1.ObjectMeta{Name:"test-0", GenerateName:"apiserver-test", Namespace:"list-paging", SelfLink:"/apis/apps/v1/namespaces/list-paging/replicasets/test-0", UID:"53b11354-2c13-4a7e-9c3e-70552b0c9e37", ResourceVersion:"14558", Generation:1, CreationTimestamp:v1.Time{Time:time.Time{wall:0x0, ext:63732091554, loc:(*time.Location)(0x7547e00)}}, DeletionTimestamp:(*v1.Time)(nil), DeletionGracePeriodSeconds:(*int64)(nil), Labels:map[string]string(nil), Annotations:map[string]string(nil), OwnerReferences:[]v1.OwnerReference(nil), Finalizers:[]string(nil), ClusterName:"", ManagedFields:[]v1.ManagedFieldsEntry{v1.ManagedFieldsEntry{Manager:"apiserver.test", Operation:"Update", APIVersion:"apps/v1", Time:(*v1.Time)(0xc00c47e4e0), FieldsType:"FieldsV1", FieldsV1:(*v1.FieldsV1)(0xc00c47e500)}}}, Spec:v1.ReplicaSetSpec{Replicas:(*int32)(0xc00c42dd3c), MinReadySeconds:0, Selector:(*v1.LabelSelector)(0xc00c47e520), Template:v1.PodTemplateSpec{ObjectMeta:v1.ObjectMeta{Name:"", GenerateName:"", Namespace:"", SelfLink:"", UID:"", ResourceVersion:"", Generation:0, CreationTimestamp:v1.Time{Time:time.Time{wall:0x0, ext:0, loc:(*time.Location)(nil)}}, DeletionTimestamp:(*v1.Time)(nil), DeletionGracePeriodSeconds:(*int64)(nil), Labels:map[string]string{"name":"test"}, Annotations:map[string]string(nil), OwnerReferences:[]v1.OwnerReference(nil), Finalizers:[]string(nil), ClusterName:"", ManagedFields:[]v1.ManagedFieldsEntry(nil)}, Spec:v1.PodSpec{Volumes:[]v1.Volume(nil), InitContainers:[]v1.Container(nil), Containers:[]v1.Container{v1.Container{Name:"fake-name", Image:"fakeimage", Command:[]string(nil), Args:[]string(nil), WorkingDir:"", Ports:[]v1.ContainerPort(nil), EnvFrom:[]v1.EnvFromSource(nil), Env:[]v1.EnvVar(nil), Resources:v1.ResourceRequirements{Limits:v1.ResourceList(nil), Requests:v1.ResourceList(nil)}, VolumeMounts:[]v1.VolumeMount(nil), VolumeDevices:[]v1.VolumeDevice(nil), LivenessProbe:(*v1.Probe)(nil), ReadinessProbe:(*v1.Probe)(nil), StartupProbe:(*v1.Probe)(nil), Lifecycle:(*v1.Lifecycle)(nil), TerminationMessagePath:"/dev/termination-log", TerminationMessagePolicy:"File", ImagePullPolicy:"Always", SecurityContext:(*v1.SecurityContext)(nil), Stdin:false, StdinOnce:false, TTY:false}}, EphemeralContainers:[]v1.EphemeralContainer(nil), RestartPolicy:"Always", TerminationGracePeriodSeconds:(*int64)(0xc00c42de00), ActiveDeadlineSeconds:(*int64)(nil), DNSPolicy:"ClusterFirst", NodeSelector:map[string]string(nil), ServiceAccountName:"", DeprecatedServiceAccount:"", AutomountServiceAccountToken:(*bool)(nil), NodeName:"", HostNetwork:false, HostPID:false, HostIPC:false, ShareProcessNamespace:(*bool)(nil), SecurityContext:(*v1.PodSecurityContext)(0xc0028373b0), ImagePullSecrets:[]v1.LocalObjectReference(nil), Hostname:"", Subdomain:"", Affinity:(*v1.Affinity)(nil), SchedulerName:"default-scheduler", Tolerations:[]v1.Toleration(nil), HostAliases:[]v1.HostAlias(nil), PriorityClassName:"", Priority:(*int32)(nil), DNSConfig:(*v1.PodDNSConfig)(nil), ReadinessGates:[]v1.PodReadinessGate(nil), RuntimeClassName:(*string)(nil), EnableServiceLinks:(*bool)(nil), PreemptionPolicy:(*v1.PreemptionPolicy)(nil), Overhead:v1.ResourceList(nil), TopologySpreadConstraints:[]v1.TopologySpreadConstraint(nil)}}}, Status:v1.ReplicaSetStatus{Replicas:0, FullyLabeledReplicas:0, ReadyReplicas:0, AvailableReplicas:0, ObservedGeneration:0, Conditions:[]v1.ReplicaSetCondition(nil)}}}}
```

## `auth` and `certificates`
These categories fail with connection errors. Authentication and SSL aspects are currently not addressed in `ignite-etcd`, so this should be investigated in the later phases of the project.

## Other
The rest of the failed test cases do not provide any meaningful error messages to explain the reasons behind the failures. Understanding those reasons would require deeper investigation and debugging of the test execution.

# Conclusion
Current `ignite-etcd` implementation passes the vast majority of integration tests, with some of the cases failing due to functionality not yet implemented.

However, there are still 5 tests which fail for reasons not yet understood:
* `apiserver/TestListResourceVersion0/watchCacheOff`
* `apiserver/TestMetadataClient`
* `apiserver/TestAPICRDProtobuf`
* `apiserver/TestTransform`
* `master/TestDynamicAudit`
