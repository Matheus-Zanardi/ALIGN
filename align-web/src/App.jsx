import { useEffect, useMemo, useState } from 'react'
import { api, clearSession, getToken, getUser, setSession } from './api'

const Icon = ({ children }) => <span className="icon">{children}</span>
const Logo = ({ compact = false }) => <div className={`brand ${compact ? 'compact' : ''}`}><div className="mark"><span></span><b>➜</b></div>{!compact && <strong>ALIGN</strong>}</div>

function Auth({ onAuth }) {
  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value })
  async function submit(e) {
    e.preventDefault(); setError(''); setLoading(true)
    try {
      if (mode === 'register') {
        await api.register(form)
        setMode('login'); setForm({ ...form, password: '' })
      } else {
        const data = await api.login({ email: form.email, password: form.password })
        setSession(data); onAuth()
      }
    } catch (e) { setError(e.message) } finally { setLoading(false) }
  }
  return <div className="auth-page">
    <div className="auth-art">
      <div className="blob one"></div><div className="blob two"></div>
      <Logo />
      <div className="auth-copy"><span className="eyebrow">SEU ESPAÇO PARA EVOLUIR</span><h1>Hoje, mais perto<br/>do seu <em>amanhã.</em></h1><p>Metas, tarefas e hábitos em um só lugar — sem pressão, no seu ritmo.</p><div className="doodle">↗ <span>um passo por dia</span></div></div>
      <div className="quote-card">“Grandes mudanças começam com pequenas ações.” <span>✦</span></div>
    </div>
    <div className="auth-panel"><div className="auth-box"><Logo compact/><div className="auth-mobile-brand">ALIGN</div><h2>{mode === 'login' ? 'Que bom te ver de novo!' : 'Comece sua jornada'}</h2><p>{mode === 'login' ? 'Entre para continuar construindo seu futuro.' : 'Crie sua conta e organize o que importa.'}</p>
      <div className="auth-tabs"><button className={mode==='login'?'active':''} onClick={()=>setMode('login')}>Entrar</button><button className={mode==='register'?'active':''} onClick={()=>setMode('register')}>Criar conta</button></div>
      <form onSubmit={submit}>{mode==='register' && <label>Seu nome<input name="name" value={form.name} onChange={change} placeholder="Como podemos te chamar?" required/></label>}<label>Email<input name="email" type="email" value={form.email} onChange={change} placeholder="seu@email.com" required/></label><label>Senha<input name="password" type="password" value={form.password} onChange={change} placeholder="••••••••" minLength="6" required/></label>{error && <div className="error">{error}</div>}<button className="primary wide" disabled={loading}>{loading?'Só um instante...':mode==='login'?'Entrar  →':'Criar minha conta  →'}</button></form>
      <small>Ao continuar, você concorda em cuidar do seu progresso com gentileza. 💜</small>
    </div></div>
  </div>
}

function Modal({ title, children, onClose }) { return <div className="modal-backdrop" onMouseDown={onClose}><div className="modal" onMouseDown={e=>e.stopPropagation()}><button className="modal-close" onClick={onClose}>×</button><h2>{title}</h2>{children}</div></div> }

function GoalForm({ goal, onSave, onClose }) {
  const [f,setF]=useState({title:goal?.title||'',description:goal?.description||'',deadline:goal?.deadline||'',completed:goal?.completed||false})
  return <form className="stack" onSubmit={e=>{e.preventDefault();onSave(f)}}><label>Título<input value={f.title} onChange={e=>setF({...f,title:e.target.value})} placeholder="Ex: Concluir a faculdade" required/></label><label>Descrição<textarea value={f.description} onChange={e=>setF({...f,description:e.target.value})} placeholder="O que você quer alcançar?"/></label><label>Prazo<input type="date" value={f.deadline||''} onChange={e=>setF({...f,deadline:e.target.value})}/></label><label className="check"><input type="checkbox" checked={f.completed} onChange={e=>setF({...f,completed:e.target.checked})}/> Meta concluída</label><div className="form-actions"><button type="button" className="ghost" onClick={onClose}>Cancelar</button><button className="primary">Salvar meta</button></div></form>
}
function HabitForm({ onSave,onClose }) { const [f,setF]=useState({title:'',description:'',frequency:'DAILY',active:true}); return <form className="stack" onSubmit={e=>{e.preventDefault();onSave(f)}}><label>Hábito<input value={f.title} onChange={e=>setF({...f,title:e.target.value})} placeholder="Ex: Ler 30 minutos" required/></label><label>Descrição<textarea value={f.description} onChange={e=>setF({...f,description:e.target.value})} placeholder="Uma pequena lembrança para você"/></label><label>Frequência<select value={f.frequency} onChange={e=>setF({...f,frequency:e.target.value})}><option value="DAILY">Todos os dias</option><option value="WEEKLY">Semanal</option></select></label><div className="form-actions"><button type="button" className="ghost" onClick={onClose}>Cancelar</button><button className="primary">Criar hábito</button></div></form> }

function Shell({ onLogout }) {
  const [page,setPage]=useState('inicio'), [dashboard,setDashboard]=useState(null), [goals,setGoals]=useState([]), [habits,setHabits]=useState([]), [loading,setLoading]=useState(true), [error,setError]=useState(''), [modal,setModal]=useState(null), [selectedGoal,setSelectedGoal]=useState(null), [tasks,setTasks]=useState([]), [stats,setStats]=useState({})
  const user=getUser()
  async function load(){setLoading(true);setError('');try{const [d,g,h]=await Promise.all([api.dashboard(),api.goals(),api.habits()]);setDashboard(d);setGoals(g||[]);setHabits(h||[])}catch(e){setError(e.message)}finally{setLoading(false)}}
  useEffect(()=>{load()},[])
  async function openGoal(goal){setSelectedGoal(goal);setPage('goal');try{setTasks(await api.tasks(goal.id)||[])}catch(e){setError(e.message)}}
  async function saveGoal(data){try{modal?.goal?await api.updateGoal(modal.goal.id,data):await api.createGoal(data);setModal(null);await load()}catch(e){setError(e.message)}}
  async function removeGoal(id){if(!confirm('Excluir esta meta?'))return;await api.deleteGoal(id);await load()}
  async function addTask(title){if(!title.trim())return;await api.createTask(selectedGoal.id,{title,description:'',completed:false});setTasks(await api.tasks(selectedGoal.id)||[]);await load()}
  async function toggleTask(t){await api.updateTask(selectedGoal.id,t.id,{...t,completed:!t.completed});setTasks(await api.tasks(selectedGoal.id)||[]);await load()}
  async function saveHabit(data){try{await api.createHabit(data);setModal(null);await load()}catch(e){setError(e.message)}}
  async function completeHabit(h){try{await api.completeHabit(h.id);const s=await api.habitStats(h.id);setStats({...stats,[h.id]:s});await load()}catch(e){setError(e.message)}}
  const progress=useMemo(()=>{if(!dashboard)return 0; const total=(dashboard.totalGoals||0)+(dashboard.totalTasks||0)+(dashboard.totalHabits||0); const done=(dashboard.completedGoals||0)+(dashboard.completedTasks||0)+(dashboard.activeHabits||0); return total?Math.round(done/total*100):0},[dashboard])
  const nav=[['inicio','⌂','Início'],['metas','◎','Metas'],['tarefas','✓','Tarefas'],['habitos','♨','Hábitos']]
  return <div className="app-shell"><aside><Logo/><nav>{nav.map(([id,ic,label])=><button key={id} className={page===id?'active':''} onClick={()=>setPage(id)}><Icon>{ic}</Icon>{label}</button>)}</nav><div className="sidebar-note"><b>✨ Seu ritmo importa</b><span>Consistência vale mais que perfeição.</span></div><div className="profile"><div className="avatar">{(user.name||'V')[0].toUpperCase()}</div><div><b>{user.name||'Você'}</b><span>{user.email}</span></div><button onClick={onLogout} title="Sair">↪</button></div></aside>
    <main><header><div><span className="mobile-logo"><Logo compact/></span><h1>{page==='inicio'?`Bom dia, ${user.name?.split(' ')[0]||'você'}! ☀️`:page==='metas'?'Suas metas':page==='tarefas'?'Tarefas':page==='habitos'?'Seus hábitos':selectedGoal?.title}</h1><p>{page==='inicio'?'Um passo de cada vez. Você está indo bem.':page==='metas'?'Transforme planos em progresso.':page==='habitos'?'Construa uma rotina que combine com você.':'Continue avançando.'}</p></div><div className="header-actions"><button className="round">♡</button><div className="avatar small">{(user.name||'V')[0].toUpperCase()}</div></div></header>
    {error&&<div className="banner">{error}<button onClick={()=>setError('')}>×</button></div>}{loading?<div className="loader">Organizando seu dia…</div>:<>
      {page==='inicio'&&<DashboardView d={dashboard||{}} goals={goals} habits={habits} progress={progress} onGoal={openGoal} go={setPage}/>} 
      {page==='metas'&&<GoalsView goals={goals} onOpen={openGoal} onAdd={()=>setModal({type:'goal'})} onEdit={g=>setModal({type:'goal',goal:g})} onDelete={removeGoal}/>} 
      {page==='tarefas'&&<AllTasks goals={goals} onGoal={openGoal}/>} 
      {page==='habitos'&&<HabitsView habits={habits} stats={stats} onAdd={()=>setModal({type:'habit'})} onComplete={completeHabit} onDelete={async id=>{if(confirm('Excluir este hábito?')){await api.deleteHabit(id);load()}}}/>} 
      {page==='goal'&&<GoalDetail goal={selectedGoal} tasks={tasks} onBack={()=>setPage('metas')} onAdd={addTask} onToggle={toggleTask}/>} 
    </>}</main>
    {modal?.type==='goal'&&<Modal title={modal.goal?'Editar meta':'Nova meta'} onClose={()=>setModal(null)}><GoalForm goal={modal.goal} onSave={saveGoal} onClose={()=>setModal(null)}/></Modal>}{modal?.type==='habit'&&<Modal title="Novo hábito" onClose={()=>setModal(null)}><HabitForm onSave={saveHabit} onClose={()=>setModal(null)}/></Modal>}
  </div>
}

function DashboardView({d,goals,habits,progress,onGoal,go}){const cards=[['◎','Metas',d.totalGoals||0,'ativas','metas'],['✓','Tarefas',d.totalTasks||0,'no total','tarefas'],['♨','Hábitos',d.totalHabits||0,'na rotina','habitos']];return <div className="content"><section className="hero-card"><div><span className="eyebrow">SEU PROGRESSO</span><h2>Continue construindo a vida<br/>que você quer viver.</h2><p>Pequenos avanços também contam. 💜</p></div><div className="progress-ring" style={{'--p':`${progress*3.6}deg`}}><div><b>{progress}%</b><span>hoje</span></div></div></section><div className="stat-grid">{cards.map(c=><button className="stat-card" key={c[1]} onClick={()=>go(c[4])}><Icon>{c[0]}</Icon><div><span>{c[1]}</span><b>{c[2]}</b><small>{c[3]}</small></div><i>→</i></button>)}</div><div className="two-col"><section className="card"><div className="section-title"><div><h3>Metas em andamento</h3><p>O que está te levando adiante.</p></div><button onClick={()=>go('metas')}>Ver todas</button></div>{goals.length?goals.slice(0,4).map(g=><button className="goal-row" key={g.id} onClick={()=>onGoal(g)}><div className="goal-emoji">{g.completed?'🌟':'🎯'}</div><div className="grow"><b>{g.title}</b><span>{g.description||'Continue avançando'}</span><div className="mini-progress"><i style={{width:g.completed?'100%':'55%'}}></i></div></div><small>{g.deadline?new Date(g.deadline+'T00:00:00').toLocaleDateString('pt-BR',{month:'short',year:'numeric'}):'sem prazo'}</small></button>):<Empty text="Sua primeira meta pode começar hoje."/>}</section><section className="card"><div className="section-title"><div><h3>Hábitos de hoje</h3><p>Cuide do presente.</p></div><button onClick={()=>go('habitos')}>Ver todos</button></div>{habits.length?habits.slice(0,5).map(h=><div className="habit-mini" key={h.id}><span>{habitEmoji(h.title)}</span><div><b>{h.title}</b><small>{h.description||'Um passo por dia'}</small></div><i>{h.active===false?'pausado':'●'}</i></div>):<Empty text="Adicione um hábito leve para começar."/>}</section></div></div>}
function GoalsView({goals,onOpen,onAdd,onEdit,onDelete}){return <div className="content"><div className="page-actions"><div className="chips"><span className="active">Todas <b>{goals.length}</b></span><span>Em andamento</span><span>Concluídas</span></div><button className="primary" onClick={onAdd}>＋ Nova meta</button></div><div className="goal-grid">{goals.map(g=><article className="goal-card" key={g.id}><div className="goal-card-top"><span>{g.completed?'🏆':'🎯'}</span><div className="menu"><button onClick={()=>onEdit(g)}>✎</button><button onClick={()=>onDelete(g.id)}>×</button></div></div><h3>{g.title}</h3><p>{g.description||'Sua próxima conquista.'}</p><div className="mini-progress big"><i style={{width:g.completed?'100%':'55%'}}></i></div><footer><span>{g.completed?'Concluída ✨':'Em progresso'}</span><small>{g.deadline||'Sem prazo'}</small></footer><button className="card-link" onClick={()=>onOpen(g)}>Abrir meta →</button></article>)}{!goals.length&&<div className="empty-large"><b>🎯</b><h3>Qual é o próximo objetivo?</h3><p>Crie uma meta e transforme vontade em passos claros.</p><button className="primary" onClick={onAdd}>Criar primeira meta</button></div>}</div></div>}
function AllTasks({goals,onGoal}){return <div className="content"><section className="card"><div className="section-title"><div><h3>Tarefas por meta</h3><p>Escolha uma meta para ver e organizar suas tarefas.</p></div></div>{goals.map(g=><button className="goal-row" key={g.id} onClick={()=>onGoal(g)}><div className="goal-emoji">✓</div><div className="grow"><b>{g.title}</b><span>{g.description}</span></div><i>→</i></button>)}{!goals.length&&<Empty text="Crie uma meta primeiro; as tarefas vivem dentro dela."/>}</section></div>}
function HabitsView({habits,stats,onAdd,onComplete,onDelete}){return <div className="content"><div className="page-actions"><div><h2 className="soft-title">Sua rotina, sem cobrança.</h2><p>Marque o que conseguiu fazer hoje.</p></div><button className="primary" onClick={onAdd}>＋ Novo hábito</button></div><div className="habit-grid">{habits.map(h=><article className="habit-card" key={h.id}><div className="habit-head"><span>{habitEmoji(h.title)}</span><button onClick={()=>onDelete(h.id)}>×</button></div><h3>{h.title}</h3><p>{h.description||'Um pouco todos os dias.'}</p><div className="week-dots">{['S','T','Q','Q','S','S','D'].map((x,i)=><i key={i} className={i<4?'done':''}>{x}</i>)}</div><div className="habit-bottom"><span>{stats[h.id]?.currentStreak ?? stats[h.id]?.streak ?? '—'} dias de sequência</span><button onClick={()=>onComplete(h)}>Concluir hoje ✓</button></div></article>)}{!habits.length&&<div className="empty-large"><b>🌱</b><h3>Comece pequeno</h3><p>Água, leitura, treino ou sono: escolha algo que faça bem para você.</p><button className="primary" onClick={onAdd}>Criar primeiro hábito</button></div>}</div></div>}
function GoalDetail({goal,tasks,onBack,onAdd,onToggle}){const [title,setTitle]=useState('');if(!goal)return null;return <div className="content"><button className="back" onClick={onBack}>← Voltar para metas</button><section className="goal-detail"><div><span className="eyebrow">META</span><h2>{goal.title}</h2><p>{goal.description}</p></div><div className="goal-badge">{goal.completed?'100%':'em andamento'}</div></section><section className="card"><div className="section-title"><div><h3>Próximos passos</h3><p>Quebre a meta em coisas possíveis.</p></div></div><form className="quick-add" onSubmit={e=>{e.preventDefault();onAdd(title);setTitle('')}}><input value={title} onChange={e=>setTitle(e.target.value)} placeholder="Adicionar uma tarefa..."/><button className="primary">＋</button></form>{tasks.map(t=><label className={`task-row ${t.completed?'done':''}`} key={t.id}><input type="checkbox" checked={!!t.completed} onChange={()=>onToggle(t)}/><span><b>{t.title}</b><small>{t.description}</small></span></label>)}{!tasks.length&&<Empty text="Adicione o primeiro passo dessa meta."/>}</section></div>}
function Empty({text}){return <div className="empty">✦ <span>{text}</span></div>}
function habitEmoji(title=''){const t=title.toLowerCase();if(t.includes('água'))return'💧';if(t.includes('ler'))return'📚';if(t.includes('trein'))return'💪';if(t.includes('estud'))return'🎓';if(t.includes('dorm'))return'🌙';return'🌱'}

export default function App(){const [authed,setAuthed]=useState(!!getToken());useEffect(()=>{const f=()=>{clearSession();setAuthed(false)};window.addEventListener('align:unauthorized',f);return()=>window.removeEventListener('align:unauthorized',f)},[]);return authed?<Shell onLogout={()=>{clearSession();setAuthed(false)}}/>:<Auth onAuth={()=>setAuthed(true)}/>}
