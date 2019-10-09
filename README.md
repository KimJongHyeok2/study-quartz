## Quartz
Terracotta라는 회사에서 개발된 Job Scheduling 라이브러리이다. 완전히 자바로 개발되어 어느 자바 애플리케이션이든 쉽게 통합해서 개발할 수 있다. 수십에서 수천 개의 작업도 실행 가능하며 간단한 Interval 형식이나 Cron 표현식으로 복잡한 스케쥴링도 지원한다.

<h3>장점</h3>
<ul>
  <li>
    DB를 기반으로 스케쥴러 간의 Clustering 기능을 제공한다.
    <ul>
      <li>시스템 Fail-over와 Round-robin 방식의 로드 분산처리를 지원한다.</li>
    </ul>
  </li>
  <li>In-Memory Job Scheduler도 제공한다.</li>
  <li>
    여러 기본 Plug-in을 제공한다.
    <ul>
      <li>ShutdownHookPlugin : JVM 종료 이벤트를 캐치해서 스케쥴러에게 알려준다,</li>
      <li>LoggingJobHistoryPlugin : Job 실행에 대한 로그를 남겨 디버깅할 때 유용하게 사용할 수 있다.</li>
    </ul>
  </li>
</ul>
<h3>단점</h3>
<ul>
  <li>Clustering 기능을 제공하지만, 단순한 Random 방식이므로 완벽한 Cluster 간의 로드 분산은 안된다.</li>
  <li>어드민 UI를 제공하지 않는다.</li>
  <li>스케쥴링 실행에 대한 History는 보관하지 않는다.</li>
  <li>Fixed Delay 타입을 보장하지 않으므로 추가 작업이 필요하다.</li>
</ul>

## Quartz 이해
자주 사용하는 용어
<h3>Job</h3>
<ul>
  <li>단 하나의 메소드를 가진(JobExecutionContext) Job 인터페이스를 제공한다. Quartz를 사용하는 개발자는 실제 수행해야 하는 작업을 이 메소드에 구현하면 된다.</li>
  <li>
    Job의 Trigger가 발생하면 스케쥴러는 JobExecutionContext 객체를 넘겨주고 execute 메소드를 호출한다.
    <ul>
      <li>JobExecutionContext : Scheduler, Trigger, JobDetail 등을 포함하여 Job 인스턴스에 대한 정보를 제공하는 객체이다.</li>
    </ul>
  </li>
</ul>
<h3>JobDetail</h3>
<ul>
  <li>Job을 실행시키기 위한 정보를 담고 있는 객체이다. Job의 이름, 그룹, JobDataMap 속성 등을 지정할 수 있다. Trigger가 Job을 수행할 때 이 정보를 기반으로 스케쥴링한다.</li>
</ul>
<h3>JobDataMap</h3>
<ul>
  <li>Job 인스턴스가 실행할 때 사용할 수 있게 원하는 정보를 담을 수 있는 객체이다.</li>
  <li>JobDetail 생성 시 JobDataMap도 같이 세팅해주면 된다.</li>
</ul>
<h3>Trigger</h3>
<ul>
  <li>Job을 실행시킬 스케쥴링 조건(반복 횟수, 시작시간) 등을 담고 있으며 Scheduler는 이 정보를 기반으로 Job을 수행시킨다.</li>
  <li>
    Trigger와 Job의 관계
    <ul>
      <li>하나의 Trigger는 반드시 하나의 Job을 지정해야한다.</li>
      <li>하나의 Job을 여러 시간대 별로 실행시킬 수 있다.</li>
    </ul>
  </li>
  <li>
    Trigger의 2가지 형태
    <ul>
      <li>SimpleTrigger : 특정 시간에 Job을 수행할 시 사용되며 반복 횟수와 실행 간격 등을 지정할 수 있다.</li>
      <li>CronTrigger : cron 표현식으로 Trigger를 정의하는 방식이며(ex. 매일 12시 - '0 0 12 * * ?') 단순 반복뿐만이 아니라 더 복잡한 스케쥴링도 지정할 수 있다.</li>
    </ul>
  </li>
</ul>
<h3>Misfire Instructions</h3>
<ul>
  <li>Misfire는 Job이 실행되어야 하는 시간, fire time을 지키지 못한 실행 불발을 의미한다.</li>
  <li>이런 Misfire는 Scheduler가 종료될 때나 쓰레드 풀에 사용 가능한 쓰레드가 없는 경우에 발생할 수 있다.</li>
  <li>
    Scheduler가 Misfire된 Trigger에 대해서 어떻게 처리할 지에 대한 다양한 policy를 지원한다.
    <ul>
      <li>MISFIRE_INSTRUCTION_FIRE_NOW : 바로 실행</li>
      <li>MISFIRE_INSTRUCTION_DO_NOTHING : 아무것도 하지 않음</li>
    </ul>
  </li>
</ul>
<h3>Listener</h3>
<ul>
  <li>
    Scheduler의 이벤트를 받을 수 있도록 Quartz에서 제공하는 인터페이스이며 2가지를 제공한다.
    <ul>
      <li>JobListener : Job 실행 전후로 이벤트를 받을 수 있다.</li>
      <li>TriggerListener : Trigger가 발생하거나 불발이 일어날 때나 Trigger를 완료할 때 이벤트를 받을 수 있다.</li>
    </ul>
  </li>
</ul>
<h3>JobStore</h3>
<ul>
  <li>
    Job과 Trigger의 정보를 2가지 방식으로 저장할 수 있다.
    <ul>
      <li>RAMJobStore : 기본 값으로 메모리에 스케쥴 정보를 저장하여 성능면에서는 제일 좋지만, 시스템 문제 발생 시 스케쥴 데이터를 유지하지 못하는 단점이 있다.</li>
      <li>JDBC JobStore : 스케쥴 정보를 DB에 저장하여 시스템에 문제가 발생하더라도 스케쥴 정보는 유지되며 시스템 재시삭 시에도 다시 Job을 실행할 수 있다.</li>
      <li>
        Quartz JobStore를 확장하여 다른 저장소(Redis, MongoDB)에도 저장할 수 있다.
        <ul>
          <li><a href="https://github.com/RedisLabs/redis-quartz">RedisJobStore</a></li>
          <li><a href="https://github.com/michaelklishin/quartz-mongodb">MonggoDBJobStore</a></li>
        </ul>
      </li>
    </ul>
  </li>
</ul>

## Quartz 구성요소
Quartz의 전체 구조와 흐름을 잘 보여주는 그림이다. Quartz의 세밀한 설정을 이해하는데 공식 문서도 도움이 되지만, 실제 소스코드를 보면 Quartz의 동작과 전체 아키텍처 구조를 이해하는데 많은 도움이 된다.<br><br>
<img src="https://user-images.githubusercontent.com/47962660/66456764-7b44f480-eaa9-11e9-945c-006b738a95bc.png"/>

## 레퍼런스
https://brunch.co.kr/@springboot/53<br>
https://advenoh.tistory.com/51?category=1012992
