# Kubernetes Integration Test Report

## Summary
- Only 5 of the hundreds of Kubernetes integration tests fail due to not implemented functionality. They fail either
  due to missing [Paging in KV/range](https://github.com/gridgain-solutions/ignite-in-k8s/issues/24) or
  missing [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
- The most critical issue is the [Watch service leaks resources](https://github.com/gridgain-solutions/ignite-in-k8s/issues/29).
  Eventually integration tests start failing due to this issue when running all the tests together.
- Sometimes integration tests start failing due to [Kubernetes integration tests fail due to healtz service timeout](https://github.com/gridgain-solutions/ignite-in-k8s/issues/27).
  The root cause is *probably* also [Watch service leaks resources](https://github.com/gridgain-solutions/ignite-in-k8s/issues/29)
  but this must be additionally investigated and confirmed since there are no `OutOfMemoryError` in the log in this 
  case.
  
All the found issues are not Ignite limitations: we need to implement the missing functionality and 
address the heap memory and thread leaks in `ignite-etcd`.

## Reproducible Failures  
| Test | Failure Cause
| -----|--------------
| `apiserver/TestListResourceVersion0/watchCacheOff` | [Paging in KV/range](https://github.com/gridgain-solutions/ignite-in-k8s/issues/24)
| `apiserver/TestAPIListChunking` | [Paging in KV/range](https://github.com/gridgain-solutions/ignite-in-k8s/issues/24)
| `apiserver/TestMetadataClient` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/TestAPICRDProtobuf` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)
| `apiserver/TestTransform` | [Historical Watch](https://github.com/gridgain-solutions/ignite-in-k8s/issues/25)

## Intermittent Failures
When running all Kubernetes integration tests in a single batch (`cd $K8S_REPO; make test-integration`) any test might 
fail due to one of the reasons:
- [Watch service leaks resources](https://github.com/gridgain-solutions/ignite-in-k8s/issues/29)
- [Kubernetes integration tests fail due to healtz service timeout](https://github.com/gridgain-solutions/ignite-in-k8s/issues/27)    
- `too many open files` error in the test log when max open files limit in the system is less than 10K. The solution is
  to increase the max open files limit.
- `ignite-etcd` failing with `OutOfMemoryError`. The solution is to increase max heap size to 16G.  

## Non-Critical Issues
These are exceptions thrown from `igntie-etcd` and logged as errors. Initial investigation showed these issues 
*probably* do no impact functionality. Need to confirm and remove the errors logging if true:
- [IgniteInterruptedCheckedException when Watch starts](https://github.com/gridgain-solutions/ignite-in-k8s/issues/26)
- [StatusRuntimeException when Watch reports events](https://github.com/gridgain-solutions/ignite-in-k8s/issues/28)
  