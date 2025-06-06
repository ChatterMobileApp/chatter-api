-- Criar tipos ENUM para status e tipo de mensagem (melhor para consistência)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'message_status_enum') THEN
        CREATE TYPE message_status_enum AS ENUM ('SENT', 'DELIVERED', 'READ');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'message_type_enum') THEN
        CREATE TYPE message_type_enum AS ENUM ('TEXT', 'IMAGE', 'AUDIO', 'VIDEO');
    END IF;
END$$;

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100),
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Conversas
CREATE TABLE IF NOT EXISTS conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    -- name VARCHAR(100), -- Pode ser útil para nome de grupos no futuro
    -- type VARCHAR(20) DEFAULT 'DIRECT', -- Para diferenciar DIRECT de GROUP no futuro
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- Para saber a última atividade
);

-- Tabela de Participantes da Conversa (tabela de ligação)
CREATE TABLE IF NOT EXISTS conversation_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    -- is_admin BOOLEAN DEFAULT FALSE, -- Para funcionalidades de grupo no futuro
    UNIQUE (user_id, conversation_id) -- Garante que um usuário só participe uma vez da mesma conversa
);

-- Tabela de Mensagens
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES users(id) ON DELETE SET NULL, -- Se o usuário for deletado, a mensagem fica com sender_id NULL ou pode ser CASCADE
    content TEXT NOT NULL,
    message_type message_type_enum DEFAULT 'TEXT',
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    status message_status_enum DEFAULT 'SENT',
    media_url TEXT -- Para mensagens de mídia no futuro
);

-- Índices para otimizar consultas comuns
CREATE INDEX IF NOT EXISTS idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX IF NOT EXISTS idx_messages_sent_at ON messages(sent_at);
CREATE INDEX IF NOT EXISTS idx_conversation_participants_user_id ON conversation_participants(user_id);
CREATE INDEX IF NOT EXISTS idx_conversation_participants_conversation_id ON conversation_participants(conversation_id);

-- Dados Iniciais de Exemplo (opcional, mas útil para desenvolvimento)

-- Usuários de exemplo
INSERT INTO users (id, phone_number, name) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '+5511999990001', 'Alice'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '+5511999990002', 'Bob'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '+5511999990003', 'Charlie')
ON CONFLICT (id) DO NOTHING;

-- Conversa de exemplo entre Alice e Bob
DO $$
DECLARE
    alice_id UUID := 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11';
    bob_id UUID := 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12';
    convo_id_alice_bob UUID := 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c11';
BEGIN
    IF NOT EXISTS (SELECT 1 FROM conversations WHERE id = convo_id_alice_bob) THEN
        INSERT INTO conversations (id, created_at, updated_at) VALUES
        (convo_id_alice_bob, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

        INSERT INTO conversation_participants (user_id, conversation_id) VALUES
        (alice_id, convo_id_alice_bob),
        (bob_id, convo_id_alice_bob);

        -- Mensagens de exemplo na conversa Alice-Bob
        INSERT INTO messages (conversation_id, sender_id, content) VALUES
        (convo_id_alice_bob, alice_id, 'Olá Bob! Tudo bem?'),
        (convo_id_alice_bob, bob_id, 'Oi Alice! Tudo ótimo, e com você?');
    END IF;
END$$;

-- Conversa de exemplo entre Alice e Charlie
DO $$
DECLARE
    alice_id UUID := 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11';
    charlie_id UUID := 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13';
    convo_id_alice_charlie UUID := 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c12';
BEGIN
    IF NOT EXISTS (SELECT 1 FROM conversations WHERE id = convo_id_alice_charlie) THEN
        INSERT INTO conversations (id, created_at, updated_at) VALUES
        (convo_id_alice_charlie, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

        INSERT INTO conversation_participants (user_id, conversation_id) VALUES
        (alice_id, convo_id_alice_charlie),
        (charlie_id, convo_id_alice_charlie);

        INSERT INTO messages (conversation_id, sender_id, content) VALUES
        (convo_id_alice_charlie, alice_id, 'E aí Charlie, pronto para o projeto?'),
        (convo_id_alice_charlie, charlie_id, 'Com certeza, Alice! Vamos nessa!');
    END IF;
END$$;

-- Função para atualizar 'updated_at' da conversa automaticamente (opcional, mas recomendado)
CREATE OR REPLACE FUNCTION update_conversation_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE conversations
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.conversation_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para chamar a função após inserir nova mensagem
DROP TRIGGER IF EXISTS trg_update_conversation_timestamp ON messages; -- Dropar se existir para evitar erro ao recriar
CREATE TRIGGER trg_update_conversation_timestamp
AFTER INSERT ON messages
FOR EACH ROW
EXECUTE FUNCTION update_conversation_updated_at();